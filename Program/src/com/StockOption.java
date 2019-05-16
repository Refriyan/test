/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Kefuoe
 */
public class StockOption extends javax.swing.JFrame {

    /**
     * Creates new form StockOption Connection variables
     */
    Connection con;                                //Object to create connection
    /*
     **Objects to update database server**
     -------------------------------------*/
    Statement stmt;
    PreparedStatement newStock;
    ResultSet rs;
    /* ---------------------------------*/
    String driverName = "com.mysql.jdbc.Driver";   //The mysql driver 
    String DBURL = "jdbc:mysql://localhost:3306/"; //Local mysql Server URL
    String DB = "restaurant";                      //Database server
    String DBUser = "root", username_admin = null, usertype;                        //Database server username
    String DBPwd = "admin";                             //Database server password
    boolean vcon = false;                          //Connection verification variable
    Random rCode = new Random();                   //Rando Generated Stock code
    Object[][] rows;
    String[] colHeader = {"ID", "CODE", "NAME", "QTY", "PRICE", "IMAGE"};

    DefaultTableModel model;
    DefaultListModel listModel = new DefaultListModel();
    InputStream img = null;
    JTableHeader h = null;
    String ImgPath = null;
    int pos = 0;
    double total = 0.0;

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
            // Logger.getLogger(StockOption.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Database Connection failed.", "Entry Error", 1);
        }
        return vcon;
    }

    // The method to add new records;
    public boolean Insertion() {
        //make sure none are blank

        if (txtProName.getText().equals("") || txtQty.getText().equals("") || txtPrice.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please make sure that none of the fields are blank.", "Entry Error", 1);
        } else if (txtQty.getText().length() <= 0) {
            JOptionPane.showMessageDialog(this, "Product Quantity cannot be zero", "Entry Error", 1);
        } else {
            try {
                //connection;

                double total = Integer.parseInt(txtQty.getText()) * (Double.parseDouble(txtPrice.getText()));
                int code = 1000 + rCode.nextInt(9999);
                String query = "INSERT INTO stock (id,code, name, qty, price,total,image) VALUES (?,?,?,?,?,?,?)";
                newStock = con.prepareStatement(query);
                newStock.setInt(1, 0);
                newStock.setInt(2, code);
                newStock.setString(3, txtProName.getText());
                newStock.setString(4, txtQty.getText());
                newStock.setBigDecimal(5, BigDecimal.valueOf(Double.parseDouble(txtPrice.getText())));
                newStock.setDouble(6, total);
                img = new FileInputStream(new File(ImgPath));
                newStock.setBlob(7, img);
                populateJTable();
                if (newStock.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(null, "Records Inserted Successfully.", "ALERT!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Records Insertion Failed", "ALERT!", JOptionPane.ERROR_MESSAGE);
                }
                con.close();

            } catch (SQLException a) {
                JOptionPane.showMessageDialog(null, "SQL EXCEPITON FAILED " + a.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);

            } catch (NumberFormatException c) {

                JOptionPane.showMessageDialog(null, "NUMBERFORMAT FAILED. " + c.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
                c.getStackTrace();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
            }
        }
        Clear();
        return true;
    }
// Check Input Fields

    public boolean checkInputs() {
        if (txtProName.getText() == null || txtPrice.getText() == null || txtQty.getText() == null) {
            return false;
        } else {
            try {
                BigDecimal.valueOf(Float.parseFloat(txtPrice.getText()));
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // The method to Update records;
    public boolean getUpdate() {
        if (checkInputs() && txt_id.getText() != null) {
            String UpdateQuery = null;
            PreparedStatement ps = null;
            BigDecimal total = BigDecimal.valueOf(Integer.parseInt(txtQty.getText()) * (Double.parseDouble(txtPrice.getText())));
            // update without image
            if (ImgPath == null) {
                try {
                    UpdateQuery = "UPDATE stock SET name=?, qty=?, price=?,total=? WHERE id = ?";
                    ps = con.prepareStatement(UpdateQuery);

                    ps.setString(1, txtProName.getText());
                    ps.setString(2, txtQty.getText());
                    ps.setString(3, txtPrice.getText());
                    ps.setBigDecimal(4, total);
                    ps.setInt(5, Integer.parseInt(txt_id.getText()));
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Records Updated Successfully.", "ALERT!", JOptionPane.ERROR_MESSAGE);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
                }

            } // update With Image
            else {
                try {
                    InputStream img = new FileInputStream(new File(ImgPath));

                    UpdateQuery = "UPDATE stock SET name=?, qty=?, price=?,total=?, image = ? WHERE id = ?";
                    ps = con.prepareStatement(UpdateQuery);
                    ps.setString(1, txtProName.getText());
                    ps.setString(2, txtQty.getText());
                    ps.setString(3, txtPrice.getText());
                    ps.setBigDecimal(4, total);
                    ps.setBlob(5, img);
                    ps.setInt(6, Integer.parseInt(txt_id.getText()));
                    ps.executeUpdate();
                    populateJTable();
                    JOptionPane.showMessageDialog(null, "Product Updated Successfully", "ALERT!", JOptionPane.ERROR_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {

            JOptionPane.showMessageDialog(null, "Make Sure That All the Fields are Field", "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }


    // The method to Delete records;
    public boolean getDelete() {
        // TODO add your handling code here:
        if (!txt_id.getText().equals("")) {
            try {

                PreparedStatement ps = con.prepareStatement("DELETE FROM stock WHERE id = ?");
                int id = Integer.parseInt(txt_id.getText());
                ps.setInt(1, id);
                ps.executeUpdate();
                populateJTable();
                JOptionPane.showMessageDialog(null, "Product Deleted Successfully", "ALERT!", JOptionPane.OK_OPTION);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Product Not Deleted", "ALERT!", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "Product Not Deleted, Make Sure ID is Entered", "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    public boolean print() {
        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM stock");

            String sql = "select code as 'Code',name as 'Name',qty as 'Qty',price as 'Price' from Stock where code='" + txtCode.getText() + "'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            model = new DefaultTableModel(rows, colHeader);
            tbStock.setModel(model);
            try {

                MessageFormat header = new MessageFormat("MyCafe's Place");
                MessageFormat footer = new MessageFormat("Page{0,number,integer}");
                tbStock.print(JTable.PrintMode.NORMAL, header, footer);
            } catch (java.awt.print.PrinterException e) {
                JOptionPane.showMessageDialog(this, "Cannot print", "Entry Error", 1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    //a method to clear all text fields after each ation
    public boolean Clear() {
        txtProName.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        txtCode.setText("");
        txtTotal.setText("");
        txt_id.setText("");
        txt_keySearch.setText("");
        return true;
    }

    public StockOption() {
        initComponents();
        DBConection();
        populateJTable();

    }

    // Function To Resize The Image To Fit Into JLabel
    public ImageIcon ResizeImage(String imagePath, byte[] pic) {
        ImageIcon myImage = null;

        if (imagePath != null) {
            myImage = new ImageIcon(imagePath);
        } else {
            myImage = new ImageIcon(pic);
        }

        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image;

    }

    //Displya records in a table
    public void populateJTable() {
        try {
            MyDbQuery mq = new MyDbQuery();
            ArrayList<Stock> list = mq.BindTable();
            Object[][] rows = new Object[list.size()][6];
            for (int i = 0; i < list.size(); i++) {
                rows[i][0] = list.get(i).getID();
                rows[i][1] = list.get(i).getCode();
                rows[i][2] = list.get(i).getName();
                rows[i][3] = list.get(i).getQte();
                rows[i][4] = list.get(i).getPrice();
                if (list.get(i).getMyImage() != null) {

                    ImageIcon image = new ImageIcon(new ImageIcon(list.get(i).getMyImage()).getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH));

                    rows[i][5] = image;
                } else {
                    rows[i][5] = null;
                }

            }

            GetData model = new GetData(rows, colHeader);
            tbStock.setModel(model);
            tbStock.setRowHeight(120);
            tbStock.getColumnModel().getColumn(5).setPreferredWidth(150);
            h = tbStock.getTableHeader();
            h.setBackground(Color.RED);
            h.setForeground(Color.BLUE);
            h.setFont(new Font("Tahome", Font.BOLD, 14));
            ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(javax.swing.JLabel.CENTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Show Data In Inputs
    public void ShowItem(int index) {
        try {
            txt_id.setText(Integer.toString(getProductList().get(index).getID()));
            txtCode.setText(Integer.toString(getProductList().get(index).getCode()));
            txtProName.setText(getProductList().get(index).getName());
            txtPrice.setText("" + getProductList().get(index).getPrice());
            txtQty.setText(Integer.toString(getProductList().get(index).getQte()));
            lbl_image.setIcon(ResizeImage(null, getProductList().get(index).getMyImage()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Display Data In JTable: 
    //      1 - Fill ArrayList With The Data
    public ArrayList<Stock> getProductList() {
        ArrayList<Stock> productList = new ArrayList<Stock>();
        //  Connection con = getConnection();
        String query = "SELECT * FROM stock";
        Statement st;
        ResultSet rs;
        try {
            st = (Statement) con.createStatement();
            rs = st.executeQuery(query);
            Stock product;
            while (rs.next()) {
                product = new Stock(
                        rs.getInt("id"),
                        rs.getInt("code"),
                        rs.getString("name"),
                        rs.getInt("qty"),
                        rs.getBigDecimal("price"),
                        rs.getBytes("image"));
                productList.add(product);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }

        return productList;

    }

    //Search method
    public void SearchData() {
        ImageIcon image1;

        try {

            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM stock WHERE code Like '" + txt_keySearch.getText().trim() + "%'");
            model = new DefaultTableModel(rows, colHeader);
            tbStock.setModel(model);

            while (rs.next()) {
                if (rs.getBytes(5) != null) {
                    ImageIcon image = new ImageIcon(new ImageIcon(rs.getBytes(5)).getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH));
                    model.addRow(new String[]{String.valueOf(rs.getInt(1)), String.valueOf(rs.getInt(2)), rs.getString(3), String.valueOf(rs.getInt(4)), rs.getBigDecimal(5).toString(), rs.getBlob(5).toString()});

                    //tbStock.revalidate();
                } else {
                    image1 = null;
                    model.addRow(new String[]{String.valueOf(rs.getInt(1)), String.valueOf(rs.getInt(2)), rs.getString(3), String.valueOf(rs.getInt(4)), rs.getBigDecimal(5).toString(), rs.getBlob(5).toString()});
                    tbStock.revalidate();
                }

            }

            tbStock.setRowHeight(120);
            tbStock.getColumnModel().getColumn(5).setPreferredWidth(150);
            h = tbStock.getTableHeader();
            h.setBackground(Color.RED);
            h.setForeground(Color.BLUE);
            h.setFont(new Font("Tahome", Font.BOLD, 18));
            ((DefaultTableCellRenderer) h.getDefaultRenderer()).setHorizontalAlignment(javax.swing.JLabel.CENTER);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "SQL EXCEPTIN FAILED", "ALERT!", JOptionPane.ERROR_MESSAGE);
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
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbStock = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        Btn_Choose_Image = new javax.swing.JButton();
        lbl_image = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtProName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        Btn_Previous = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_keySearch = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "STOCK OPTION PANEL", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14), new java.awt.Color(0, 51, 204))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "RECORDS VIEW", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14), new java.awt.Color(0, 51, 255))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(0, 51, 255));

        tbStock.setAutoCreateRowSorter(true);
        tbStock.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        tbStock.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tbStock.setFillsViewportHeight(true);
        tbStock.setGridColor(new java.awt.Color(0, 51, 255));
        tbStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStockMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbStock);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 756, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 756, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 586, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "STOCK ENTRY", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 2, 14), new java.awt.Color(0, 51, 255))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Pro ID");

        txt_id.setEditable(false);
        txt_id.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        Btn_Choose_Image.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Btn_Choose_Image.setText("Upload Image");
        Btn_Choose_Image.setBorder(null);
        Btn_Choose_Image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_Choose_ImageActionPerformed(evt);
            }
        });

        lbl_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/menu/fish.gif"))); // NOI18N
        lbl_image.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 51, 255)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Product");

        txtProName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Quantity");

        txtQty.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtQty.setText("0");
        txtQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQtyActionPerformed(evt);
            }
        });
        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtyKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Price");

        txtPrice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrice.setText("0.00");
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPriceKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPriceKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Code");

        txtCode.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Total");

        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Images/Add5.gif"))); // NOI18N
        jButton1.setToolTipText("Add New Stock");
        jButton1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Images/Edit5.gif"))); // NOI18N
        jButton2.setToolTipText("Edit Stock");
        jButton2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Images/Icon Cancel.png"))); // NOI18N
        jButton7.setToolTipText("Clear all fields");
        jButton7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        Btn_Previous.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cart/resultset_first.png"))); // NOI18N
        Btn_Previous.setToolTipText("Check Previous Product");
        Btn_Previous.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Btn_Previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Btn_PreviousActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Images/Delete5.gif"))); // NOI18N
        jButton3.setToolTipText("Delete Stock");
        jButton3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cart/resultset_last.png"))); // NOI18N
        jButton5.setToolTipText("Check Next Product");
        jButton5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Images/Print5.gif"))); // NOI18N
        jButton4.setToolTipText("Print Records");
        jButton4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/Images/Exit5.gif"))); // NOI18N
        jButton6.setToolTipText("Exit ");
        jButton6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(45, 45, 45))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(38, 38, 38)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProName, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(65, 65, 65)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(62, 62, 62)
                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(64, 64, 64)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Btn_Previous, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(Btn_Choose_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_image, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txt_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_image, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(Btn_Choose_Image, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtProName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Btn_Previous, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Search By Code");

        txt_keySearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txt_keySearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_keySearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txt_keySearch, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_keySearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Btn_Choose_ImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_Choose_ImageActionPerformed
        // TODO add your handling code here:
        try {
            JFileChooser file = new JFileChooser();
            file.setCurrentDirectory(new File(System.getProperty("user.home")));

            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images", "jpg", "png", "gif");
            file.addChoosableFileFilter(filter);
            int result = file.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = file.getSelectedFile();
                String path = selectedFile.getAbsolutePath();
                lbl_image.setIcon(ResizeImage(path, null));
                ImgPath = path;
            } else {
                JOptionPane.showMessageDialog(null, "No File Selected", "ALERT!", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_Btn_Choose_ImageActionPerformed

    private void tbStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStockMouseClicked
        // TODO add your handling code here:
        int index = tbStock.getSelectedRow();
        ShowItem(index);
    }//GEN-LAST:event_tbStockMouseClicked

    private void txt_keySearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_keySearchKeyReleased
        // TODO add your handling code here:
        SearchData();
    }//GEN-LAST:event_txt_keySearchKeyReleased

    private void Btn_PreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Btn_PreviousActionPerformed
        // TODO add your handling code here:
        pos--;

        if (pos < 0) {
            pos = 0;
        }

        ShowItem(pos);

    }//GEN-LAST:event_Btn_PreviousActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        pos++;

        if (pos >= getProductList().size()) {
            pos = getProductList().size() - 1;
        }

        ShowItem(pos);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        // DeleteAllDBRecords();//Invoke Update method
        print();
        // removeItems();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        getDelete();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Insertion();//Invoke insertion method

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        getUpdate();//Invoke Update method
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        Clear();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        try {
            this.dispose();
            frmMainMenu admin = new frmMainMenu();
            admin.adm = username_admin;
            admin.admtype = usertype;
            admin.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getStackTrace(), "ALERT!", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyTyped

    }//GEN-LAST:event_txtPriceKeyTyped

    private void txtPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtPriceKeyPressed

    private void txtPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriceKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtPriceKeyReleased

    private void txtQtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_txtQtyKeyReleased

    private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyActionPerformed

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
            java.util.logging.Logger.getLogger(StockOption.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockOption.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockOption.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockOption.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StockOption().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Btn_Choose_Image;
    private javax.swing.JButton Btn_Previous;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_image;
    private javax.swing.JTable tbStock;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProName;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_keySearch;
    // End of variables declaration//GEN-END:variables
}
