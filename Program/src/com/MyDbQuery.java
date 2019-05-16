/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

/**
 *
 * @author Kefuoe
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Query;

/**
 *
 * @author 1bestcsharp.blogspot.com
 */
public class MyDbQuery {

    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/restaurant", "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public ArrayList<Stock> BindTable() {

        ArrayList<Stock> list = new ArrayList<Stock>();
        Connection con = getConnection();
        Statement st;
        ResultSet rs;

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT `id`, `code`,`name`, `qty`, `price`,`image` FROM `stock`");
            Stock p;
            while (rs.next()) {
                p = new Stock(
                        rs.getInt("id"),
                        rs.getInt("code"),
                        rs.getString("name"),
                        rs.getInt("qty"),
                        rs.getBigDecimal("price"),
                        rs.getBytes("image")
                );
                list.add(p);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MyDbQuery.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

   
}
