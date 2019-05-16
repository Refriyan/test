/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Kefuoe
 */
public class FrmChefOrWaiterPanel extends javax.swing.JFrame {

    /**
     * Creates new form FrmCashierOrWaiterPanel
     */
    Connection con;
    Statement stmt;
    PreparedStatement newStock;
    ResultSet rs;
    /* ---------------------------------*/
    String driverName = "com.mysql.jdbc.Driver";   //The mysql driver 
    String DBURL = "jdbc:mysql://localhost:3306/"; //Local mysql Server URL
    String DB = "restaurant";                      //Database server
    String DBUser = "root";                        //Database server username
    String DBPwd = "admin";                             //Database server password
    boolean vcon = false;
    DefaultTableModel model;
    String[][] records;
    String username_admin = null, usertype;
    String[] colHeader = {"ORDER ID", "TABLE NO", "DATE", "CUSTOMER NO", "COST", "DETAILS"};
    FrmFeedBack feedback = new FrmFeedBack();
    double result = 0.00;
    double payment = 0.00, change = 0.00;

    public FrmChefOrWaiterPanel() {
        initComponents();
        DBConection();
        GetRecords();
        txt_id.setEnabled(false);
    }

    /**
     * The connection method
     *
     * @return
     */
    public boolean DBConection() {
        try {
            //connect to database
            Class.forName(driverName);
            con = DriverManager.getConnection(DBURL + DB, DBUser, DBPwd);
            if (con.equals(con)) {
                vcon = true;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed." + ex.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);

        }
        return vcon;
    }
     public boolean GetRecords() {
        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM orderfood WHERE orderState='Pending'");
            model = new DefaultTableModel(records, colHeader);
            jTable1.setModel(model);
            while (rs.next()) {
                model.addRow(new String[]{rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(9)});
                jTable1.revalidate();
            }
            // total.setText(""+model.getRowCount());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQL EXCEPTION FAILED." + ex.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);

        }
        return true;
    }

    public boolean Update() {
        //update of order
        if (txt_id.getText() == null) {

        } else {
            try {
                String query = "UPDATE `restaurant`.`orderfood` SET `orderState` = ? WHERE `orderfood`.`orderCode` =?";
                newStock = con.prepareStatement(query);
                newStock.setString(1, cmStatus.getSelectedItem().toString());
                newStock.setString(2, txt_id.getText());

                if (newStock.executeUpdate() == 1) {

                    JOptionPane.showMessageDialog(null, "Order Has Been Paid successfully.", "ALERT!", JOptionPane.ERROR_MESSAGE);
                } else {

                    JOptionPane.showMessageDialog(null, "Order Payment failed  Enter Product Code Please", "ALERT!", JOptionPane.ERROR_MESSAGE);
                }
                GetRecords();

            } catch (SQLException a) {
                JOptionPane.showMessageDialog(null, "SQL EXCEPTION FAILED." + a.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
                a.printStackTrace();
            } catch (NumberFormatException c) {
                JOptionPane.showMessageDialog(null, "NUMBERFORMAT fail." + c.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
                c.printStackTrace();
            }

        }
        return true;

    }

    // Show Data In Inputs
    public void ShowItem(int index) {
        TableModel model1 = jTable1.getModel();
        int[] indexs = jTable1.getSelectedRows();
        try {
            for (int i = 0; i < indexs.length; i++) {
                txt_id.setText("" + model1.getValueAt(indexs[i], 0));
                txtTableNo.setText("" + model1.getValueAt(indexs[i], 1));
                txtCID.setText("" + model1.getValueAt(indexs[i], 3));
                result += Double.parseDouble(""+model1.getValueAt(indexs[i], 4));
                txtCost.setText("" +result);
                 

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cmStatus = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        btnUpdateOrder = new javax.swing.JButton();
        txt_id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTableNo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCost = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPayment = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtChange = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "CHEF & WAITER ORDER VIEW", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14), new java.awt.Color(0, 51, 255))); // NOI18N

        jTable1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 51, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.setGridColor(new java.awt.Color(0, 153, 0));
        jTable1.setRowHeight(40);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        cmStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Completed" }));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Order State Update");

        btnUpdateOrder.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnUpdateOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cart/report_disk.png"))); // NOI18N
        btnUpdateOrder.setText("UPDATE");
        btnUpdateOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateOrderActionPerformed(evt);
            }
        });

        txt_id.setEditable(false);
        txt_id.setForeground(new java.awt.Color(255, 0, 0));
        txt_id.setText(" ");

        jLabel2.setText("Order ID");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Customer Tel:");

        txtCID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCID.setText(" ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Table No:");

        txtTableNo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTableNo.setText(" ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Total Cost");

        txtCost.setEditable(false);
        txtCost.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCost.setForeground(new java.awt.Color(0, 153, 51));
        txtCost.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCostMouseClicked(evt);
            }
        });
        txtCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostActionPerformed(evt);
            }
        });
        txtCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCostKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCostKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Payment:");

        txtPayment.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPayment.setForeground(new java.awt.Color(0, 51, 204));
        txtPayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPaymentKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Change:");

        txtChange.setEditable(false);
        txtChange.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtChange.setForeground(new java.awt.Color(255, 0, 0));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cart/money.png"))); // NOI18N
        jButton2.setText("Pay Order");
        jButton2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(cmStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnUpdateOrder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(155, 155, 155))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCID, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTableNo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 897, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtTableNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(btnUpdateOrder)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int index = jTable1.getSelectedRow();
        ShowItem(index);
         
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnUpdateOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateOrderActionPerformed
        // TODO add your handling code here:
        Update();
        txt_id.setText("");
    }//GEN-LAST:event_btnUpdateOrderActionPerformed

    private void txtCostMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCostMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCostMouseClicked

    private void txtCostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostActionPerformed

    private void txtCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCostKeyPressed

    private void txtCostKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtCostKeyReleased

    private void txtPaymentKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPaymentKeyReleased
        // TODO add your handling code here:
        try {

            payment = Integer.parseInt(txtPayment.getText());
            change = (payment - result);
            txtChange.setText("" + change);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);

        }
    }//GEN-LAST:event_txtPaymentKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
     
      if(txtPayment.getText()!=null){
      
         JOptionPane.showMessageDialog(null, "Payment Made Successfully.", "ALERT!", JOptionPane.ERROR_MESSAGE);
           this.dispose();
      }else{ 
          JOptionPane.showMessageDialog(null, "Payment Failed", "ALERT!", JOptionPane.ERROR_MESSAGE);}
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(FrmChefOrWaiterPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmChefOrWaiterPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmChefOrWaiterPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmChefOrWaiterPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmChefOrWaiterPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUpdateOrder;
    private javax.swing.JComboBox cmStatus;
    public javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtCID;
    public javax.swing.JTextField txtChange;
    public javax.swing.JTextField txtCost;
    public javax.swing.JTextField txtPayment;
    private javax.swing.JTextField txtTableNo;
    private javax.swing.JTextField txt_id;
    // End of variables declaration//GEN-END:variables
}
