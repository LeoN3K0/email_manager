package com.leon3k0;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        db.connectDatabase();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

}
