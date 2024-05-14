/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectweb;

/**
 *
 * @author abozen
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProjectJoin {
    public boolean joinProject(String projectKey, int currentUserID) {
        // Verilen anahtarın projects tablosunda olup olmadığını kontrol et
        int projectID = getProjectIDByProjectKey(projectKey);
        if (projectID != -1) {
            // projects tablosunda proje bulundu, bu durumda users_projects tablosuna ekle
            insertUserProject(currentUserID, projectID);
            System.out.println("Proje başarıyla katıldınız.");
            return true;
        } else {
            // projects tablosunda proje bulunamadı
            System.out.println("Belirtilen anahtara sahip proje bulunamadı.");
            return false;
        }
    }

    private int getProjectIDByProjectKey(String projectKey) {
        int projectID = -1; // -1 değeri, proje bulunamadığını göstermek için kullanılabilir
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "SELECT project_id FROM projects WHERE project_key = '" + projectKey + "'";
            rs = st.executeQuery(query);
            if (rs.next()) {
                projectID = rs.getInt("project_id");
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

        return projectID;
    }

    private void insertUserProject(int userID, int projectID) {
        Connection con = null;
        Statement st = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "INSERT INTO users_projects (user_id, project_id) VALUES (" +
                    userID + ", " + projectID + ")";
            st.executeUpdate(query);
            System.out.println("Proje başarıyla kullanıcıya atandı.");
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

