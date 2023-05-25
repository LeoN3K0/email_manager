package com.leon3k0;

import java.sql.*;
import java.util.Properties;

public class Database {
    Connection c = null;
    public void connectDatabase() {
        try {
            String url = "jdbc:postgresql://localhost/email_gen";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "left4dead2");
            
            c = DriverManager.getConnection(url, props);
    
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}