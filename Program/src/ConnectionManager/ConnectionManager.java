package ConnectionManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Jasman Pardede
 */


public class ConnectionManager {
    private Connection con;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/restaurant";  // myDB --> nama database kita
    private final String username = "root";       // user name DMBS
    private final String password = "admin";    // pswd DMBS

    public Connection logOn(){
        try {
            //Load JDBC Driver
            Class.forName( driver ).newInstance();
            //Buat object Connection
            con = DriverManager.getConnection( url, username, password );
        }
        catch(ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex){
        }
        return con;
    }

    public void logOff(){
        try {
            //Tutup Koneksi
            con.close();
        }
        catch(SQLException ex){
        }
    }
}
