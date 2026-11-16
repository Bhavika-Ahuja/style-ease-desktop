package com.stylesync;

import com.stylesync.view.LoginView;
import com.stylesync.util.DatabaseConnection;

import javax.swing.*;

/**
 * Main entry point for StyleSync application
 */
public class Main {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to MongoDB. Please check your configuration.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Launch application
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
