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
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            Statement st = con.createStatement();
            String query = "SELECT * FROM users WHERE user_id = 1 AND password = '123'";

            ResultSet rs = st.executeQuery(query);
            if (rs.next()) 
                System.out.println("success");
            else
                System.out.println("fail");
            
        }catch(HeadlessException | SQLException E){
            System.out.println("error: " + E.getMessage());
        }
    }
    
}
