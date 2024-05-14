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
            String log = "!! " + username + " is in chat";
            server.SendBroadCastMessage(log.getBytes());
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
                    String log = username + " -> " + message;
                    server.SendBroadCastMessage(log.getBytes());
                    //Frm_Server.lst_messagesFromClient_model.addElement(log);//this.socket.getPort() + "->" + message); 
                } else if(message.startsWith("!!PRIVATE")){
                    String messageTo = message.split(":")[1];
                    message = message.substring(11 + messageTo.length());
                    message = (String) username + " -> " + message;
                    server.SendMessage(message.getBytes(), findIdFromUsername(messageTo));
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

            msg[msg.length - 1] = 0x14;
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
            System.out.println("disconnected");
            //Frm_Server.lst_messagesFromClient_model.addElement(" !! " + username + " has left");
            //Frm_Server.lst_clients_model.removeElement(username);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    int findIdFromUsername(String username)
    {
        for (SClient sClient : server.clientList) {
            if (username.equals(sClient.username) ) {
                return sClient.id;
            }
        }
        return -1; //error
    }

}
