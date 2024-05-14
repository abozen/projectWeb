/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author abozen
 */
public class Test {

    static Server server = new Server();

    public static void main(String[] args) {
        int port = 5002;
        server.Create(port);
        server.Listen();
    }
}
