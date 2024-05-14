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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    // her clientın bir soketi olamlı
    private ServerSocket serverSocket;
    public boolean isListening = false;
    public int clientId = 0;

    // port numarası
    private int port;

    public ArrayList<SClient> clientList;

    //yapıcı metod
    public Server() {

    }

    // client başlatma
    public boolean Create(int port) {

        try {
            this.port = port;
            // Client Soket nesnesi
            serverSocket = new ServerSocket(this.port);
            clientList = new ArrayList<>();

        } catch (Exception err) {
            System.out.println("Error connecting to server: " + err);
        }
        return true;
    }

    @Override
    public void run() {
        try {
            while (this.isListening) {
                System.out.println("Server waiting client...");
                Socket clientSocket = this.serverSocket.accept();//blocking
                SClient nsclient = new SClient(clientSocket, this);
                nsclient.Listen();
                clientList.add(nsclient);
                this.clientId++;
                String cinfo = nsclient.id + "|" + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort();

            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void DicconnectClient(SClient client) {
        this.clientList.remove(client);
        //Frm_Server.lst_clients_model.removeAllElements();
        for (SClient sClient : clientList) {
            String cinfo = sClient.socket.getInetAddress().toString() + ":" + sClient.socket.getPort();

            //Frm_Server.lst_clients_model.addElement(cinfo);
        }

    }

    public void Listen() {
        this.isListening = true;
        this.start();
        /*try {
            Socket clientSocket = this.serverSocket.accept();
            DataInputStream sInput = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream sOutput = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("Connection accepted " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            byte[] messageByte = new byte[1024];
            int bytesRead = sInput.read(messageByte);
            String message = new String(messageByte, 0, bytesRead);
            System.out.println(message);

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }*/

    }

    public void Stop() {
        try {
            this.isListening = false;
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //mesaj gönderme fonksiyonu
    public void SendMessage(byte[] msg, int clientId) {
        msg[msg.length - 1] = 0x14;
        for (SClient sClient : this.clientList) {
            if (clientId == sClient.id) {
                sClient.SendMessage(msg);
                break;
            }
        }

    }

    public void SendBroadCastMessage(byte[] msg) {
        for (SClient sClient : this.clientList) {
            msg[msg.length - 1] = 0x14;
            sClient.SendMessage(msg);
        }
    }

}

