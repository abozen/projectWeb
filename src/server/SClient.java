/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author abozen
 */
import java.awt.HeadlessException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SClient extends Thread {

    String username;
    // her clientın bir soketi olamlı
    public int id;
    public Socket socket;
    private Server server;
    // gönderilecek alınacak bilgileri byte dizisine çevirmek için
    private DataInputStream sInput;
    //private DataInputStream sInput;
    private DataOutputStream sOutput;

    public boolean isListening = false;

    SQL_Operations sql = new SQL_Operations();

    //yapıcı metod
    public SClient(Socket socket, Server server) {

        try {
            this.server = server;
            this.socket = socket;
            this.sInput = new DataInputStream(this.socket.getInputStream());
            this.sOutput = new DataOutputStream(this.socket.getOutputStream());
            this.id = server.clientId;

        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Listen() {
        this.isListening = true;
        try {

            byte[] messageByte = new byte[31];
            int bytesRead = sInput.read(messageByte);
            String message = new String(messageByte, 0, bytesRead);
            username = message;
            System.out.println(message);
            //Frm_Server.lst_clients_model.addElement(username);
            //String log = "!! " + username + " is in chat";
            //server.SendBroadCastMessage(log.getBytes());
            //Frm_Server.lst_messagesFromClient_model.addElement(log);

        } catch (IOException ex) {
            this.Disconnect();
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.start();

    }

    @Override
    public void run() {
        try {
            while (this.isListening) {
                byte[] messageByte = new byte[31];
                int bytesRead = sInput.read(messageByte);
                String message = new String(messageByte, 0, bytesRead);
                System.out.println(message);
                if (message.startsWith("!!DISCONNECT")) {
                    Disconnect();
                    break;
                } else if (message.startsWith("!!BROADCAST")) {
                    broadcast(message);
                } else if (message.startsWith("!!PRIVATE")) {
                    String messageTo = message.split(":")[1];
                    message = message.substring(11 + messageTo.length());
                    message = (String) username + " -> " + message;
                    server.SendMessage(message.getBytes(), findIdFromUsername(messageTo));
                } else if (message.startsWith("!!QUERY")) { // !!QUERY:register:0:aburo:123
                    String type = message.split(":")[1];
                    switch (type) {
                        case "register":
                            registerQuery(message);
                            break;
                        case "login":
                            loginQuery(message);
                            break;
                        case "projectList":
                            projectListQuery(message);
                            break;
                        case "createProject":
                            createProjectQuery(message);
                            break;
                        case "joinProject":
                            joinProjectQuery(message);
                            break;
                        case "activeMembers":
                            activeMembersQuery(message);
                            break;
                        default:
                            break;

                    }
                } else if (message.startsWith("!!DISCONNECT")) {
                    Disconnect();
                }

            }

        } catch (IOException ex) {
            this.Disconnect();
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//mesaj gönderme fonksiyonu
    public void SendMessage(byte[] msg) {
        try {

            //msg[msg.length - 1] = 0x14;
            sOutput.write(msg);
        } catch (IOException err) {
            System.out.println("Exception writing to server: " + err);
        }
    }

    //clientı kapatan fonksiyon
    public void Disconnect() {
        try {
            this.isListening = false;
            this.socket.close();
            this.sInput.close();
            this.sOutput.close();
            this.server.DicconnectClient(this);
            System.out.println("disconnected " + id);
            //Frm_Server.lst_messagesFromClient_model.addElement(" !! " + username + " has left");
            //Frm_Server.lst_clients_model.removeElement(username);

        } catch (IOException ex) {
            Logger.getLogger(Server.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isClientConnected() {
        if (socket.isConnected() && !socket.isClosed()) {
            System.out.println("Socket bağlı.");
            return true;
        } else {
            System.out.println("Socket bağlı değil.");
            return false;
        }
    }

    int findIdFromUsername(String username) {
        for (SClient sClient : server.clientList) {
            if (username.equals(sClient.username)) {
                return sClient.id;
            }
        }
        return -1; //error
    }

    void registerQuery(String message) {

        String[] splittedMsg = message.split(":");
        int id = Integer.parseInt(splittedMsg[2]);
        String username = splittedMsg[3];
        String password = splittedMsg[4];

        Connection con = null;
        Statement st = null;

        try {
            // Bağlantıyı oluştur
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            // Aynı kullanıcı adıyla kayıtlı bir kullanıcının olup olmadığını kontrol et
            String checkQuery = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet checkResult = st.executeQuery(checkQuery);

            if (checkResult.next()) {
                System.out.println("Bu kullanıcı adı zaten mevcut.");
            } else {
                // Mevcut en yüksek kullanıcı kimliğini al
                String maxIDQuery = "SELECT MAX(user_id) FROM users";
                ResultSet rs = st.executeQuery(maxIDQuery);
                int maxID = 0;
                if (rs.next()) {
                    maxID = rs.getInt(1); // En yüksek kullanıcı kimliğini al
                    System.out.println("Max user_id: " + maxID);
                } else {
                    System.out.println("ResultSet boş.");
                }

                // Kullanıcıyı ekle
                String insertQuery = "INSERT INTO users (user_id, username, password) VALUES (" + (++maxID) + ", '" + username + "', '" + password + "')";
                int rowsAffected = st.executeUpdate(insertQuery);

                if (rowsAffected > 0) {
                    System.out.println("Kullanıcı başarıyla eklendi.");
                } else {
                    System.out.println("Kullanıcı eklenemedi.");
                }
            }
        } catch (SQLException e) {
            System.out.println("error: " + e.getMessage());
        } finally {
            // Kaynakları kapat
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void loginQuery(String message) {
        String[] splittedMsg = message.split(":");
        String username = splittedMsg[2];
        String password = splittedMsg[3];
        try {

            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            Statement st = con.createStatement();
            String query = "SELECT * FROM users WHERE username = '" + username
                    + "' AND password = '" + password + "'";

            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                int userID = rs.getInt("user_id"); // Kullanıcı id al
                this.id = userID;
                System.out.println("success");
                String msg = "!!login+:" + userID;
                System.out.println(msg);
                server.SendMessage(msg.getBytes(), this.id);

            } else {
                System.out.println("fail");
            }

        } catch (HeadlessException | SQLException E) {
            System.out.println("error: " + E.getMessage());
        }

    }

    private void projectListQuery(String message) {
        String[] splittedMsg = message.split(":");
        int userID = Integer.parseInt(splittedMsg[2]);
        //String username = splittedMsg[2];
        //int userID = sql.getUserIDFromUsername(username);
        String projectList = sql.getUserProjectsInfo(userID);
        String sendMsg = "!!projectList:" + projectList;
        server.SendMessage(sendMsg.getBytes(), this.id);

    }

    private void createProjectQuery(String message) {
        String[] splittedMsg = message.split(":");
        int userID = Integer.parseInt(splittedMsg[2]);
        String projectName = splittedMsg[3];
        String projectKey = sql.createNewProject(projectName, userID);
        String sendMsg = "!!createProject:" + projectName + ":" + projectKey;
        server.SendMessage(sendMsg.getBytes(), this.id);
    }

    private void joinProjectQuery(String message) {
        String[] splittedMsg = message.split(":");
        int userID = Integer.parseInt(splittedMsg[2]);
        String key = splittedMsg[3];
        boolean isJoined = sql.joinProject(key, userID);
        String sendMsg = "!!joinProject:" + isJoined;
        server.SendMessage(sendMsg.getBytes(), this.id);
    }

    private ArrayList<Integer> activeMembersQuery(String message) {
        String[] splittedMsg = message.split(":");
        int projectID = Integer.parseInt(splittedMsg[2]);
        ArrayList<Integer> userIDs = sql.getUserIDsForProject(projectID);
        ArrayList<Integer> activeUserIDs = new ArrayList<Integer>();
        for (int id : userIDs) {
            for (SClient client : server.clientList) {
                if (id == client.id && client.isClientConnected()) {
                    activeUserIDs.add(id);
                }
            }
        }
        String names = "";
        for (int id : activeUserIDs) {
            names += sql.getUsername(id);
            names += ":";
        }

        String sendMsg = "!!activeMembers:" + names;
        server.SendMessage(sendMsg.getBytes(), this.id);

        return activeUserIDs;
        //String activeMembers = sql.getUsernamesForProject(projectID);
        // String sendMsg = "!!activeMembers:"+activeMembers;
        //server.SendMessage(sendMsg.getBytes(), this.id);
    }

    private void broadcast(String message) {
        String[] splittedMsg = message.split(":");
        int userID = Integer.parseInt(splittedMsg[1]);
        int currentProjectID = Integer.parseInt(splittedMsg[2]);
        String chatMsg = splittedMsg[3];
        String username = sql.getUsername(userID);
        ArrayList<Integer> activeUserIDs = activeMembersQuery("x:x:" + currentProjectID);
        String log = "!!BROADCAST:" + username + " -> " + chatMsg;
        server.SendBroadCastMessage(log.getBytes(), activeUserIDs);
        //Frm_Server.lst_messagesFromClient_model.addElement(log);//this.socket.getPort() + "->" + message);
    }

}
