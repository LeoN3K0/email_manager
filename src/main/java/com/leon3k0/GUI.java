package com.leon3k0;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
    private DatabaseManager databaseTB = new DatabaseManager();

    public GUI() {
        setTitle("Business Email Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        createUI();

        // Add a component listener to the frame for resizing events
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeTable();
            }
        });
    }

    private void createUI() {
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
        gbc.anchor = GridBagConstraints.NORTH;
        JButton addButton = new JButton("Add Record");
        addButton.addActionListener(this);
        formPanel.add(addButton, gbc);

        gbc.gridy = 7;        
        JButton delButton = new JButton("Delete Record");
        delButton.addActionListener(this);
        formPanel.add(delButton, gbc);

        gbc.gridy = 8;
        gbc.weighty = 1.0;
        JButton exButton = new JButton("Export Table");
        exButton.addActionListener(this);
        formPanel.add(exButton, gbc);

        mainPanel.add(formPanel, BorderLayout.WEST);

        dataTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(375, 300));
        Border border = BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                new LineBorder(Color.BLACK)
        );
        scrollPane.setBorder(border);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        tableModel = new DefaultTableModel();
        dataTable.setModel(tableModel);
        dataTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        updateTableModel();

        add(mainPanel);

        pack();
    }

    private void resizeTable() {
        // Set the table's preferred size based on the available space
        int tableWidth = getContentPane().getWidth() - 200; // Adjust the width as needed
        int tableHeight = getContentPane().getHeight() - 20; // Adjust the height as needed
        dataTable.setPreferredScrollableViewportSize(new Dimension(tableWidth, tableHeight));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Record")) {
            addRecord();
        } else if (e.getActionCommand().equals("Delete Record")) {
            deleteRecord();
        } else if (e.getActionCommand().equals("Export Table")) {
            exportTable();
        }
    }

    private void addRecord(){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String company = companyField.getText();
        EmailGenerator emailGenerator = new EmailGenerator();
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !company.isEmpty()) {
            String email = emailGenerator.generateEmail(firstName, lastName, company);
            String password = passwordGenerator.generateRandomPassword();
            databaseTB.insertData(firstName, lastName, email, password);

            updateTableModel();
            if (emailField != null) {
                emailField.setText(email);
            }
            if (passwordField != null) {
                passwordField.setText(password);
            }
            firstNameField.setText("");
            lastNameField.setText("");
            companyField.setText("");
        } else {
            JOptionPane.showMessageDialog(rootPane, "Fields can't be blank, please fill out all fields.");
        }
    }

    private void deleteRecord() {
        int col = 0;

        if (dataTable.getSelectedRow() != -1) {
            int row = dataTable.getSelectedRow();
            String val = dataTable.getValueAt(row, col).toString();
            int id = Integer.parseInt(val);

            databaseTB.deleteData(id);
            updateTableModel();
        } else {
            JOptionPane.showMessageDialog(rootPane, "No Record Selected, Please Select a Record from the Table.");
        }
    }

    private void exportTable() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            databaseTB.saveCSV(file);
        }
    }

    private void updateTableModel() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try (ResultSet resultSet = databaseTB.fetchData()) {
            ResultSetMetaData metaData = databaseTB.fetchMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update table: " + e.getMessage());
        }
    }
}
