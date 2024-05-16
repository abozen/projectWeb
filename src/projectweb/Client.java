/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectweb;

/**
 *
 * @author abozen
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

public class Client implements Runnable {

    String username;
    // her clientın bir soketi olamlı
    private Socket socket;
    // gönderilecek alınacak bilgileri byte dizisine çevirmek için
    private DataInputStream sInput;
    //private DataInputStream sInput;
    private DataOutputStream sOutput;

    // server adresi 
    private String server;
    // port numarası
    private int port;
    boolean isListening = false;

    fLogin loginFrame = null;
    fMenu menuFrame = null;
    fProject projectFrame = null;

    String[][] projectInfos = null;
    public DefaultListModel<String> listModel;

    //yapıcı metod
    public Client(String server, int port, String username, fLogin loginFrame) {
        this.username = username;
        this.server = server;
        this.port = port;
        this.loginFrame = loginFrame;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                // Program kapatıldığında socket bağlantısını kapat
                System.out.println("program shutting down");
                disconnect();
            }
        });

    }

    public void setMenuFrame(fMenu menuFrame) {
        this.menuFrame = menuFrame;
    }

    public void setProjectFrame(fProject projectFrame) {
        this.projectFrame = projectFrame;
    }

    // client başlatma
    public boolean ConnectToServer() {

        try {
            // Client Soket nesnesi
            socket = new Socket(this.server, this.port);
            sInput = new DataInputStream(socket.getInputStream());
            sOutput = new DataOutputStream(socket.getOutputStream());
            this.Listen();
            System.out.println("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
            SendMessage(username.getBytes());

        } catch (Exception err) {
            System.out.println("Error connecting to server: " + err);
        }
        return true;
    }

    public void Listen() {
        if (isListening) {
            return;
        }
        this.isListening = true;
        Thread t1 = new Thread(this);
        t1.start();

    }

    @Override
    public void run() {
        try {
            while (this.isListening) {
                byte[] messageByte = new byte[128];
                int bytesRead = sInput.read(messageByte);
                String message = new String(messageByte, 0, bytesRead);
                System.out.println("client side: " + message);
                if (message.startsWith("!!login+")) {
                    int id = Integer.parseInt(message.split(":")[1]);
                    loginFrame.login(id);
                }else if(message.startsWith("!!login-")){
                    loginFrame.loginFailed();
          
                } else if (message.startsWith("!!projectList")) {
                    String projectMsg = message.substring(14);
                    projectInfos = getProjectInfos(projectMsg);
                    listModel = getProjectList(projectInfos);
                    menuFrame.setProjectList(listModel, projectInfos);
                } else if (message.startsWith("!!createProject")) {
                    String[] splittedMsg = message.split(":");
                    String projectName = splittedMsg[1];
                    String projectKey = splittedMsg[2];
                    menuFrame.projectCreated(projectName, projectKey);
                } else if (message.startsWith("!!joinProject")) {
                    String[] splittedMsg = message.split(":");
                    boolean isJoined = false;
                    if (splittedMsg[1].equals("true")) {
                        isJoined = true;
                    }
                    menuFrame.joinedProject(isJoined);
                } else if (message.startsWith("!!activeMembers")) {
                    String[] splittedMsg = message.split(":");
                    String[] names = new String[splittedMsg.length - 1];
                    for (int i = 0; i < names.length; i++) {
                        names[i] = splittedMsg[i + 1];
                    }
                    projectFrame.setActiveMembers(names);
                } else if(message.startsWith("!!BROADCAST")) 
                {
                    String[] splittedMsg = message.split(":");
                    int currentProjectID = Integer.parseInt(splittedMsg[1]);
                    String chatMsg = splittedMsg[2];
                    projectFrame.writeChatMessage(chatMsg, currentProjectID);
                }else if(message.startsWith("!!PRIVATE")){
                    String[] splittedMsg = message.split(":");
                    int currentProjectID =Integer.parseInt( splittedMsg[1]);
                    String chatMsg = splittedMsg[2];
                    projectFrame.writePrivateMessage(chatMsg, currentProjectID);
                }

                //Frm_Client.lst_messagesFromServer_model.addElement(message);
            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //mesaj gönderme fonksiyonu
    public void SendMessage(byte[] msg) {
        try {
            //mesaj göndermek için daha once oluşmuii output nesnesi kullanılıyor

            //msg[msg.length - 1] = 0x14;
            sOutput.write(msg);
        } catch (IOException err) {
            System.out.println("Exception writing to server: " + err);
        }
    }

    //clientı kapatan fonksiyon
    public void disconnect() {
        try {
            String message = "!!DISCONNECT:"+loginFrame.currentUserID;
            SendMessage(message.getBytes());
            //tüm nesneleri kapatıyoruz
            if (sInput != null) {
                sInput.close();
            }
            if (sOutput != null) {
                sOutput.close();
            }

            if (socket != null) {
                socket.close();
            }

        } catch (Exception err) {
            System.out.println(err.getMessage());
        }

    }

    private String[][] getProjectInfos(String projectMsg) {
        System.out.println(projectMsg);
        String[] infos = projectMsg.split(":");
        String[][] projectInfos = new String[infos.length / 4][4];
        for (int i = 0; i < infos.length / 4; i++) {
            for (int j = 0; j < 4; j++) {
                projectInfos[i][j] = infos[i * 4 + j];
            }
        }
        return projectInfos;
    }

    private DefaultListModel<String> getProjectList(String[][] projectInfos) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < projectInfos.length; i++) {
            model.addElement(projectInfos[i][2]);
        }

        return model;
    }

}
