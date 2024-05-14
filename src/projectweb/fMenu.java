/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectweb;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author abozen
 */
public class fMenu extends javax.swing.JFrame {

    /**
     * Creates new form fMenu
     */
    private static int currentUserID;

    public fMenu(int currentUserID) {
        this.currentUserID = currentUserID;
        initComponents();
        // fMenu penceresini oluştur
        // users_projects tablosunu sorgulayarak kullanıcıya ait proje kimliklerini al
        List<Integer> projectIDs = getProjectIDsForUser(currentUserID);
        // Elde edilen proje kimliklerini kullanarak proje isimlerini al
        List<String> projectNames = getProjectNames(projectIDs);
        // Elde edilen proje isimlerini JList'e atayarak göster
        displayProjectNames(projectNames);
    }

    private List<Integer> getProjectIDsForUser(int userID) {
        List<Integer> projectIDs = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            String query = "SELECT project_id FROM users_projects WHERE user_id = " + userID;
            rs = st.executeQuery(query);

            while (rs.next()) {
                int projectID = rs.getInt("project_id");
                projectIDs.add(projectID);
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

        return projectIDs;
    }

    private List<String> getProjectNames(List<Integer> projectIDs) {
        List<String> projectNames = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:derby://localhost:1527/shopmedb;create=true");
            st = con.createStatement();

            for (int projectID : projectIDs) {
                String query = "SELECT project_name FROM projects WHERE project_id = " + projectID;
                rs = st.executeQuery(query);
                if (rs.next()) {
                    String projectName = rs.getString("project_name");
                    projectNames.add(projectName);
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

        return projectNames;
    }

    private void displayProjectNames(List<String> projectNames) {
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String projectName : projectNames) {
            model.addElement(projectName);
        }
        list_projects.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        b_back = new javax.swing.JButton();
        b_newProject1 = new javax.swing.JButton();
        txt_key = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_projects = new javax.swing.JList<>();
        b_joinProject1 = new javax.swing.JButton();
        b_openProject1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_projectName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 400));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_back.setText("Back");
        getContentPane().add(b_back, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 70, 30));

        b_newProject1.setText("New Project");
        b_newProject1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_newProject1ActionPerformed(evt);
            }
        });
        getContentPane().add(b_newProject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 120, 30));
        getContentPane().add(txt_key, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 227, 100, 30));

        jLabel1.setText("Project Key");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 80, 20));

        list_projects.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list_projects);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 170, 200));

        b_joinProject1.setText("Join a Project");
        b_joinProject1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_joinProject1ActionPerformed(evt);
            }
        });
        getContentPane().add(b_joinProject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 270, 120, 30));

        b_openProject1.setText("Open Project");
        getContentPane().add(b_openProject1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 120, 30));

        jLabel2.setText("PROJECTS");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, -1));
        getContentPane().add(txt_projectName, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, 100, -1));

        jLabel3.setText("Project Name");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_newProject1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_newProject1ActionPerformed
        // TODO add your handling code here:
        ProjectCreation pc = new ProjectCreation();
        String projectName = txt_projectName.getText();
        String projectKey = pc.createNewProject(projectName, currentUserID);
        String log = "A project named " + projectName + " is created. Project Key is : " + projectKey;
        JOptionPane.showMessageDialog(null, log);
        this.invalidate();
        this.validate();
        this.repaint();
    }//GEN-LAST:event_b_newProject1ActionPerformed

    private void b_joinProject1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_joinProject1ActionPerformed
        // TODO add your handling code here:
        ProjectJoin pj = new ProjectJoin();
        boolean isJoined = pj.joinProject(txt_key.getText(), currentUserID);
        if (isJoined) {
            String log = "Projeye başarıyla katıldınız.";
            JOptionPane.showMessageDialog(null, log);
        } else {
            String log = "Belirtilen anahtara sahip proje bulunamadı.";
            JOptionPane.showMessageDialog(null, log);
        }
        this.invalidate();
        this.validate();
        this.repaint();
    }//GEN-LAST:event_b_joinProject1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(fMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fMenu(currentUserID).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_back;
    private javax.swing.JButton b_joinProject1;
    private javax.swing.JButton b_newProject1;
    private javax.swing.JButton b_openProject1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<String> list_projects;
    private javax.swing.JTextField txt_key;
    private javax.swing.JTextField txt_projectName;
    // End of variables declaration//GEN-END:variables
}