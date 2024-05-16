/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package projectweb;

import javax.swing.DefaultListModel;

/**
 *
 * @author abozen
 */
public class fProject extends javax.swing.JFrame {

    /**
     * Creates new form fProject
     */
    static int currentUserID;
    static Client client;
    static int projectID;
    static String projectName;

    public fProject(int currentUserID, Client client, int projectID, String projectName) {
        initComponents();
        this.currentUserID = currentUserID;
        this.client = client;
        this.projectID = projectID;
        this.projectName = projectName;
        projectName_label.setText(projectName);
        listActiveMembers();
    }

    private void listActiveMembers() {
        String message = "!!QUERY:activeMembers:" + projectID;
        client.SendMessage(message.getBytes());
    }

    public void setActiveMembers(String[] usernames) {
        System.out.println("modeling");
        DefaultListModel<String> model = new DefaultListModel<>();
        for (int i = 0; i < usernames.length; i++) {
            model.addElement(usernames[i]);
        }
        activeMembers_list.setModel(model);
    }

    public void writeChatMessage(String message, int currentProjectID) {
        if (this.projectID != currentProjectID) {
            System.out.println("proje açık olmadığı için yazılmadı");
            return;
        }
        String chat = chatbox.getText();
        chat += "\n" + message;
        chatbox.setText(chat);
    }

    public void writePrivateMessage(String message, int currentProjectID) {
        if (this.projectID != currentProjectID) {
            System.out.println("proje açık olmadığı için yazılmadı");
            return;
        }
        String chat = chatbox2.getText();
        chat += "\n" + message;
        chatbox2.setText(chat);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        projectName_label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        activeMembers_list = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        chat_txt2 = new javax.swing.JTextArea();
        b_send = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        chatbox2 = new javax.swing.JTextArea();
        b_refresh = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        chatbox = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        chat_txt = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        projectName_label.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        projectName_label.setText("project name");
        projectName_label.setToolTipText("");
        getContentPane().add(projectName_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 190, 20));

        activeMembers_list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(activeMembers_list);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 110, 130));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        chat_txt2.setColumns(20);
        chat_txt2.setRows(5);
        jScrollPane3.setViewportView(chat_txt2);

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 210, 40));

        b_send.setText("Send Message");
        b_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_sendActionPerformed(evt);
            }
        });
        getContentPane().add(b_send, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 350, -1, -1));

        jLabel2.setText("Active Members");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jLabel3.setText("Chat Box");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, -1, -1));

        jButton1.setText("Send Private Message");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 440, 170, -1));

        chatbox2.setEditable(false);
        chatbox2.setBackground(new java.awt.Color(223, 222, 222));
        chatbox2.setColumns(20);
        chatbox2.setRows(5);
        chatbox2.setDisabledTextColor(new java.awt.Color(252, 196, 196));
        jScrollPane4.setViewportView(chatbox2);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 180, 150));

        b_refresh.setText("Refresh");
        b_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_refreshActionPerformed(evt);
            }
        });
        getContentPane().add(b_refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        chatbox.setEditable(false);
        chatbox.setBackground(new java.awt.Color(223, 222, 222));
        chatbox.setColumns(20);
        chatbox.setRows(5);
        chatbox.setDisabledTextColor(new java.awt.Color(252, 196, 196));
        jScrollPane5.setViewportView(chatbox);

        getContentPane().add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 250, 170));

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        chat_txt.setColumns(20);
        chat_txt.setRows(5);
        jScrollPane6.setViewportView(chat_txt);

        getContentPane().add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 250, 60));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_refreshActionPerformed
        // TODO add your handling code here:
        listActiveMembers();
    }//GEN-LAST:event_b_refreshActionPerformed

    private void b_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_sendActionPerformed
        // TODO add your handling code here:
        String message = chat_txt.getText();
        String query = "!!BROADCAST:" + currentUserID + ":" + projectID + ":" + message;
        client.SendMessage(query.getBytes());
        chat_txt.setText("");
    }//GEN-LAST:event_b_sendActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(activeMembers_list.isSelectionEmpty())
        {
            System.out.println("biri seçili değil");
            return;
        }
        String chatMsg = chat_txt2.getText();
        String recieverUsername = activeMembers_list.getSelectedValue();
        String log = "!!PRIVATE:" + currentUserID + ":" + recieverUsername + ":" + projectID + ":" + chatMsg;
        client.SendMessage(log.getBytes());
        chat_txt2.setText("");

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(fProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fProject.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fProject(currentUserID, client, projectID, projectName).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> activeMembers_list;
    private javax.swing.JButton b_refresh;
    private javax.swing.JButton b_send;
    private javax.swing.JTextArea chat_txt;
    private javax.swing.JTextArea chat_txt2;
    private javax.swing.JTextArea chatbox;
    private javax.swing.JTextArea chatbox2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel projectName_label;
    // End of variables declaration//GEN-END:variables
}
