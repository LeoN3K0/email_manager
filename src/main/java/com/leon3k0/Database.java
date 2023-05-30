package com.leon3k0;

import java.sql.*;
import java.util.Properties;
import java.sql.ResultSet;

public class Database {
    Connection c = null;
    Statement stmt = null;
    public void connectDatabase(String dbName) {
        try {
            String url = "jdbc:postgresql://localhost/" + dbName;
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "left4dead2");
            
            c = DriverManager.getConnection(url, props);
            c.setAutoCommit(false);
    
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void insertDatabase(String fName, String lName, String email, String tPass){
        try {
            Database db = new Database();
            db.connectDatabase("email_gen");
            stmt = db.c.createStatement();

            String sql = "INSERT INTO NEW_HIRE (FIRST_NAME,LAST_NAME,EMAIL,TEMP_PASS) "
            + "VALUES ('"+fName+"', '"+lName+"', '"+email+"', '"+tPass+"');";
            stmt.executeUpdate(sql);
            stmt.close();
            db.c.commit();
            db.c.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    public ResultSet fetchData(){
        ResultSet rs = null;
        try {
            Database db = new Database();
            db.connectDatabase("email_gen");
            stmt = db.c.createStatement();

            rs = stmt.executeQuery( "SELECT * FROM new_hire;" );            
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return rs;
    }

    public ResultSetMetaData fetchMetaData(){
        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        try {
            Database db = new Database();
            db.connectDatabase("email_gen");
            stmt = db.c.createStatement();

            rs = stmt.executeQuery( "SELECT * FROM new_hire;" );
            metaData = rs.getMetaData();
            return metaData;                        
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return  metaData;
    }
}