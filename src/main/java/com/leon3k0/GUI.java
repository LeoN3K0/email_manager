package com.leon3k0;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class GUI extends JFrame implements ActionListener {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField companyField;
    private JTextField emailField;
    private JTextField passwordField;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private Database databaseTB = new Database();

    public GUI() {
        setTitle("Business Email Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        emailField = new JTextField(); // Initialize emailField
        passwordField = new JTextField(); // Initialize passwordField

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Add padding between fields

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(new Dimension(200, 20));
        formPanel.add(firstNameField, gbc);

        gbc.gridy = 2;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(new Dimension(200, 20));
        formPanel.add(lastNameField, gbc);

        gbc.gridy = 4;
        formPanel.add(new JLabel("Company:"), gbc);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        companyField = new JTextField();
        companyField.setPreferredSize(new Dimension(200, 20));
        formPanel.add(companyField, gbc);

        gbc.gridy = 6;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;
        JButton addButton = new JButton("Add Record");
        addButton.addActionListener(this);
        formPanel.add(addButton, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        dataTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(375, 300)); // Set the initial preferred size of the scroll pane
        Border border = BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10), // Add spacing (10 pixels) around the table
                new LineBorder(Color.BLACK) // Add a black line border around the table
        );
        scrollPane.setBorder(border);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        tableModel = new DefaultTableModel();

        // Retrieve column names from the ResultSet metadata
        ResultSetMetaData metaData = databaseTB.fetchMetaData();
        int columnCount;
        ResultSet resultSet = databaseTB.fetchData();
        try {
            columnCount = metaData.getColumnCount();
            for (int i = 2; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Retrieve data rows from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 2; i <= columnCount; i++) {
                    rowData[i - 2] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        dataTable.setModel(tableModel);

        add(mainPanel);

        pack();

        // Add a component listener to the frame for resizing events
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeTable();
            }
        });
    }

    private void resizeTable() {
        // Set the table's preferred size based on the available space
        int tableWidth = getContentPane().getWidth() - 200; // Adjust the width as needed
        int tableHeight = getContentPane().getHeight() - 20; // Adjust the height as needed
        dataTable.setPreferredScrollableViewportSize(new Dimension(tableWidth, tableHeight));
    }

    public void actionPerformed(ActionEvent e) {
        String firstName;
        String lastName;
        String email;
        String password;

        if (e.getActionCommand().equals("Add Record")) {
            firstName = firstNameField.getText();
            lastName = lastNameField.getText();
            String company = companyField.getText();
            Email_Gen newEmail = new Email_Gen();
            Password_Gen newPass = new Password_Gen();
            Database newDatabase = new Database();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !company.isEmpty()) {
                email = newEmail.generateEmail(firstName, lastName, company);
                password = newPass.generateRandomPassword();
                newDatabase.insertDatabase(firstName, lastName, email, password);

                updateTableModel();
                if (emailField != null) { // Check if emailField is not null
                    emailField.setText(email); // Set email value to emailField
                }
                if (passwordField != null) { // Check if passwordField is not null
                    passwordField.setText(password); // Set password value to passwordField
                }
                firstNameField.setText("");
                lastNameField.setText("");
                companyField.setText("");
            } else {
                JOptionPane.showMessageDialog(rootPane, "Fields can't be blank, please fill out all fields.");
            }
        }
    }

    private void updateTableModel() {
        // Clear the existing data in the table model
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        ResultSetMetaData metaData = databaseTB.fetchMetaData();
        int columnCount;
        ResultSet resultSet = databaseTB.fetchData();
        try {
            columnCount = metaData.getColumnCount();
            for (int i = 2; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Retrieve data rows from the ResultSet
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 2; i <= columnCount; i++) {
                    rowData[i - 2] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
