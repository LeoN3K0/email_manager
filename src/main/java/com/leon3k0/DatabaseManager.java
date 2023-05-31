package com.leon3k0;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import javax.swing.JOptionPane;

import java.sql.ResultSet;

public class DatabaseManager {
    private Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", DatabaseConfig.username);
        props.setProperty("password", DatabaseConfig.password);

        if(DatabaseConfig.isDemoMode()){
            return DriverManager.getConnection(DatabaseConfig.url);
        }
        else {
            return DriverManager.getConnection(DatabaseConfig.url, props);
        }
        
    }

    public void insertData(String fName, String lName, String email, String tPass){
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO NEW_HIRE (FIRST_NAME, LAST_NAME, EMAIL, TEMP_PASS) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, email);
            stmt.setString(4, tPass);

            stmt.executeUpdate();

            System.out.println("Record created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to insert data: " + e.getMessage());
        }
    }

    public void deleteData(int id){
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM NEW_HIRE WHERE ID = ?")) {
                
            stmt.setInt(1, id);

            int rowCount = stmt.executeUpdate();
            if (rowCount > 0) {
                System.out.println("Record deleted successfully");
            } else {
                System.out.println("Record not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to delete data: " + e.getMessage());
        }
    }

    public ResultSet fetchData(){
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            return stmt.executeQuery("SELECT * FROM new_hire");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch data: " + e.getMessage());
        }
        return null;        
    }

    public ResultSetMetaData fetchMetaData(){
        ResultSetMetaData metaData = null;

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM new_hire")) {

            metaData = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch metadata: " + e.getMessage());
        }

        return metaData;
    }

    public void saveCSV (File file){
        String filePath = file.getAbsolutePath();
        if(!filePath.toLowerCase().endsWith(".csv")){
            filePath +=".csv";
            file = new File(filePath);
        }
        try (Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM new_hire");        
        FileWriter fileWriter = new FileWriter(file)) {

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Write CSV header
        for (int i = 1; i <= columnCount; i++) {
            fileWriter.append(metaData.getColumnName(i));
            if (i < columnCount) {
                fileWriter.append(",");
            }
        }
        fileWriter.append("\n");

        // Write CSV data
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                fileWriter.append(rs.getString(i));
                if (i < columnCount) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");
        }
        JOptionPane.showMessageDialog(null, "CSV file saved successfully!");
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save CSV file: " + ex.getMessage());
        }
    }
}