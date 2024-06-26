/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author abozen
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class SQL_Operations {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int KEY_LENGTH = 5;

    public String getUserProjectsInfo(int userID) {
        StringBuilder projectsInfo = new StringBuilder();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            // Kullanıcıya bağlı proje kimliklerini al
            String query = "SELECT project_id FROM users_projects WHERE user_id = " + userID;
            rs = st.executeQuery(query);

            // Her proje için ilgili bilgileri al
            while (rs.next()) {
                int projectID = rs.getInt("project_id");
                String projectInfo = getProjectInfo(projectID, userID);
                if (!projectInfo.isEmpty()) {
                    projectsInfo.append(projectInfo).append(":");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (rs != null) {
                    rs.close();
                }
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

        // Son projenin sonundaki ":" karakterini kaldırın
        if (projectsInfo.length() > 0) {
            projectsInfo.deleteCharAt(projectsInfo.length() - 1);
        }

        return projectsInfo.toString();
    }

    private String getProjectInfo(int projectID, int userID) {
        StringBuilder projectInfo = new StringBuilder();

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            // Proje bilgilerini al
            String query = "SELECT * FROM projects WHERE project_id = " + projectID;
            rs = st.executeQuery(query);

            // Proje bilgilerini dizeye ekleyin
            if (rs.next()) {
                
                String projectName = rs.getString("project_name");
                String projectKey = rs.getInt("admin_id") == userID ? rs.getString("project_key") : "xx";
                int adminID = rs.getInt("admin_id");

                projectInfo.append(projectID).append(":").append(projectKey).append(":")
                        .append(projectName).append(":").append(adminID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return projectInfo.toString();
    }

    public int getUserIDFromUsername(String username) {
        int userID = -1; // Varsayılan olarak -1, kullanıcı bulunamazsa kullanılacak

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            // Kullanıcı adına göre kullanıcı kimliğini al
            String query = "SELECT user_id FROM users WHERE username = '" + username + "'";
            rs = st.executeQuery(query);

            // Kullanıcı bulunduysa, kullanıcı kimliğini al
            if (rs.next()) {
                userID = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return userID;
    }

    public String createNewProject(String projectName, int currentUserID) {
        // Random bir proje anahtarı oluştur
        String projectKey = generateRandomKey();

        // Mevcut kullanıcıyı admin olarak kabul et ve en yüksek project_id'den bir fazla al
        int adminUserID = currentUserID; // Örneğin, admin kullanıcı kimliği sabit bir değer olarak alınsın
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
                if (rs != null) {
                    rs.close();
                }
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

        return maxProjectID + 1; // En yüksek proje kimliğinden bir fazla olarak yeni proje kimliğini döndür
    }

    private void insertProjectToDatabase(String projectName, String projectKey, int adminUserID, int projectID) {
        Connection con = null;
        Statement st = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "INSERT INTO projects (project_id, project_key, project_name, admin_id) VALUES ("
                    + projectID + ", '" + projectKey + "', '" + projectName + "', " + adminUserID + ")";
            st.executeUpdate(query);
            System.out.println("Yeni proje başarıyla oluşturuldu.");
        } catch (SQLException e) {
            e.printStackTrace();
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

    private void insertProjectToUserProjectsTable(int userID, int projectID) {
        Connection con = null;
        Statement st = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "INSERT INTO users_projects (user_id, project_id) VALUES ("
                    + userID + ", " + projectID + ")";
            st.executeUpdate(query);
            System.out.println("Yeni proje başarıyla kullanıcıya atandı.");
        } catch (SQLException e) {
            e.printStackTrace();
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
                if (rs != null) {
                    rs.close();
                }
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

        return projectID;
    }

    private void insertUserProject(int userID, int projectID) {
        Connection con = null;
        Statement st = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();
            String query = "INSERT INTO users_projects (user_id, project_id) VALUES ("
                    + userID + ", " + projectID + ")";
            st.executeUpdate(query);
            System.out.println("Proje başarıyla kullanıcıya atandı.");
        } catch (SQLException e) {
            e.printStackTrace();
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

    public ArrayList<Integer> getUserIDsForProject(int projectID) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            // Belirli bir proje kimliği ile ilişkili kullanıcı kimliklerini al
            String query = "SELECT user_id FROM users_projects WHERE project_id = " + projectID;
            rs = st.executeQuery(query);

            // Kullanıcı kimliklerini bir int dizisine ekleyin
            
            ArrayList<Integer> userIDs = new ArrayList<Integer>();
            
            while (rs.next()) {
                int userID = rs.getInt("user_id");
                userIDs.add(userID);
            }
            return userIDs;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (rs != null) {
                    rs.close();
                }
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
        // Hata durumunda veya sonuç bulunamadığında null döndür
        return null;
    }



    public String getUsername(int userID) {
        String username = null;

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            // Belirli bir user_id ile ilişkili kullanıcı adını al
            String query = "SELECT username FROM users WHERE user_id = " + userID;
            rs = st.executeQuery(query);

            // Kullanıcı adını al
            if (rs.next()) {
                username = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Kaynakları kapat
            try {
                if (rs != null) {
                    rs.close();
                }
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

        return username;
    }
}
