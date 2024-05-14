/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projectweb;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author abozen
 */
public class ProjectWeb {

    /**
     * @param args the command line arguments
     */
    static Server server = new Server();
    public static void main(String[] args) {
        int port = 5002;
        server.Create(port);
        server.Listen();
    }
    
}
