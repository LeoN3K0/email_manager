package com.leon3k0;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField companyField;
    private JTextField emailField;
    private JTextField passwordField;

    public GUI(){
        setTitle("Business Email Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Add padding between fields

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        firstNameField = new JTextField();
        mainPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        lastNameField = new JTextField();
        mainPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Company:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        companyField = new JTextField();
        mainPanel.add(companyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        emailField = new JTextField();
        emailField.setEditable(false);
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        passwordField = new JTextField();
        passwordField.setEditable(false);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton generateButton = new JButton("Generate");
        generateButton.addActionListener(this);
        mainPanel.add(generateButton, gbc);

        add(mainPanel);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Generate")) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String company = companyField.getText();
            Email_Gen newEmail = new Email_Gen();
            Password_Gen newPass = new Password_Gen();

            if (!firstName.isEmpty() && !lastName.isEmpty() && !company.isEmpty() )
            {
                String email = newEmail.generateEmail(firstName, lastName, company);
                emailField.setText(email);

                String password = newPass.generateRandomPassword();
                passwordField.setText(password);
                
            }
            else{

                JOptionPane.showMessageDialog(rootPane, "Fields can't be blank, please fill out all fields.");
            }
            
        }
    }
}
