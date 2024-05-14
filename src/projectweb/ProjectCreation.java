/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectweb;

/**
 *
 * @author abozen
 */
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProjectCreation {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int KEY_LENGTH = 5;

    public String createNewProject(String projectName, int currentUserID) {
        // Random bir proje anahtarı oluştur
        String projectKey = generateRandomKey();
        
        // Mevcut kullanıcıyı admin olarak kabul et ve en yüksek project_id'den bir fazla al
        int adminUserID = 1; // Örneğin, admin kullanıcı kimliği sabit bir değer olarak alınsın
        int newProjectID = getNextProjectID();

        // Yeni proje bilgilerini projects tablosuna ekle
        insertProjectToDatabase(projectName, projectKey, adminUserID, newProjectID);

        // Yeni proje bilgilerini users_projects tablosuna ekle
        insertProjectToUserProjectsTable(currentUserID, newProjectID);
        
        return projectKey;
    }

    private String generateRandomKey() {
        Random random = new Random();
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            keyBuilder.append(CHARACTERS.charAt(index));
        }
        return keyBuilder.toString();
    }

    private int getNextProjectID() {
        int maxProjectID = 0;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "SELECT MAX(project_id) FROM projects";
            rs = st.executeQuery(query);
            if (rs.next()) {
                maxProjectID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return maxProjectID + 1; // En yüksek proje kimliğinden bir fazla olarak yeni proje kimliğini döndür
    }

    private void insertProjectToDatabase(String projectName, String projectKey, int adminUserID, int projectID) {
        Connection con = null;
        Statement st = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "INSERT INTO projects (project_id, project_key, project_name, admin_id) VALUES (" +
                    projectID + ", '" + projectKey + "', '" + projectName + "', " + adminUserID + ")";
            st.executeUpdate(query);
            System.out.println("Yeni proje başarıyla oluşturuldu.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertProjectToUserProjectsTable(int userID, int projectID) {
        Connection con = null;
        Statement st = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "INSERT INTO users_projects (user_id, project_id) VALUES (" +
                    userID + ", " + projectID + ")";
            st.executeUpdate(query);
            System.out.println("Yeni proje başarıyla kullanıcıya atandı.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
