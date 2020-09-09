/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ConnectionDB {

    public static Connection getConnect() throws SQLException {
//        Connection conn = null;
//        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLHH";
//        String user = "sa";
//        String pass = "YourPassword123";
//        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//
//        try {
//            Class.forName(driver);
//            conn = DriverManager.getConnection(url, user, pass);
//            System.out.println("connection success");
//
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return conn;
        Connection conn= null;
        String url="jdbc:mysql://localhost:3306/qlhh?serverTimezone=UTC";
        String Driver="com.mysql.cj.jdbc.Driver";
        String user ="sa";
        String password="1234$";
        try {
            Class.forName(Driver);
            conn=DriverManager.getConnection(url, user,password);
            System.out.println("Connection success");
            
        } catch (ClassNotFoundException ex) {
            System.out.println("Connection error");
        }
        
        return conn;
    }

    public static void main(String[] args) throws SQLException {
        getConnect();
    }

}
