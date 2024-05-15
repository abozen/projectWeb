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
    
    String[][] projectInfos = null;
    public DefaultListModel<String> listModel;
    

    //yapıcı metod
    public Client(String server, int port, String username, fLogin loginFrame) {
        this.username = username;
        this.server = server;
        this.port = port;
        this.loginFrame = loginFrame;

    }
    
    public void setMenuFrame(fMenu menuFrame)
    {
       this.menuFrame = menuFrame;
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
                byte[] messageByte = new byte[1024];
                int bytesRead = sInput.read(messageByte);
                String message = new String(messageByte, 0, bytesRead);
                System.out.println("client side: "+message);
                if(message.startsWith("!!login+")){
                    int id = Integer.parseInt(message.split(":")[1]);
                    loginFrame.login(id);
                }else if(message.startsWith("!!projectList"))
                {
                    String projectMsg = message.substring(14);
                    projectInfos = getProjectInfos(projectMsg);
                    listModel = getProjectList(projectInfos);
                    menuFrame.setProjectList(listModel);
                }else if(message.startsWith("!!createProject"))
                {
                    String[] splittedMsg = message.split(":");
                    String projectName = splittedMsg[1];
                    String projectKey = splittedMsg[2];
                    menuFrame.projectCreated(projectName, projectKey);
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
            String disconnect = "!!DISCONNECT";
            SendMessage(disconnect.getBytes());
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
        String[][] projectInfos = new String[infos.length/4][4];
        for (int i = 0; i < infos.length/4; i++) {
            for (int j = 0; j < 4; j++) {
                projectInfos[i][j] = infos[i*4 + j];
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
