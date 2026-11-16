package com.stylesync.view;

import com.stylesync.controller.RegisterController;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Registration screen with modern design
 */
public class RegisterView extends JFrame {
    private RegisterController controller;
    private ModernTextField fullNameField;
    private ModernTextField usernameField;
    private ModernTextField emailField;
    private ModernPasswordField passwordField;
    private ModernPasswordField confirmPasswordField;

    public RegisterView() {
        this.controller = new RegisterController(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("StyleSync - Create Account");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new GridLayout(1, 2)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(230, 220, 250),
                        getWidth(), getHeight(), new Color(255, 240, 250)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(true);

        // Left panel - Branding
        JPanel leftPanel = createBrandingPanel();

        // Right panel - Register form
        JPanel rightPanel = createRegisterPanel();

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);
    }

    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = StyleSyncColors.createGradient(getWidth(), getHeight());
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        // Logo and branding
        JLabel logoIcon = new JLabel("✨");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("StyleSync");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 48));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Look Good, Feel Great!");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        tagline.setForeground(new Color(255, 255, 255, 230));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline2 = new JLabel("Live Better.");
        tagline2.setFont(new Font("Segoe UI", Font.ITALIC, 20));
        tagline2.setForeground(new Color(255, 255, 255, 230));
        tagline2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heartIcon = new JLabel("💜");
        heartIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        heartIcon.setForeground(Color.WHITE);
        heartIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(logoIcon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(appName);
        panel.add(Box.createVerticalStrut(20));
        panel.add(tagline);
        panel.add(tagline2);
        panel.add(Box.createVerticalStrut(40));
        panel.add(heartIcon);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Join StyleSync today!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 20, 20);
        panel.add(subtitleLabel, gbc);

        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Full Name field
        fullNameField = new ModernTextField("Full Name");
        gbc.gridy = 2;
        panel.add(fullNameField, gbc);

        // Username field
        usernameField = new ModernTextField("Username");
        gbc.gridy = 3;
        panel.add(usernameField, gbc);

        // Email field
        emailField = new ModernTextField("Email Address");
        gbc.gridy = 4;
        panel.add(emailField, gbc);

        // Password field
        passwordField = new ModernPasswordField("Password");
        gbc.gridy = 5;
        panel.add(passwordField, gbc);

        // Confirm Password field
        confirmPasswordField = new ModernPasswordField("Confirm Password");
        gbc.gridy = 6;
        panel.add(confirmPasswordField, gbc);

        // Sign Up button
        ModernButton signUpButton = new ModernButton("Sign Up");
        signUpButton.addActionListener(e -> controller.handleRegister());
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 20, 15, 20);
        panel.add(signUpButton, gbc);

        // Login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setOpaque(false);
        JLabel haveAccountLabel = new JLabel("Already have an account?");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        haveAccountLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        JLabel loginLabel = new JLabel("<html><u>Login</u></html>");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLabel.setForeground(StyleSyncColors.PRIMARY);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.showLoginView();
            }
        });

        loginPanel.add(haveAccountLabel);
        loginPanel.add(loginLabel);

        gbc.gridy = 8;
        gbc.insets = new Insets(10, 20, 20, 20);
        panel.add(loginPanel, gbc);

        return panel;
    }

    public String getFullName() {
        return fullNameField.getText().trim();
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearFields() {
        fullNameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
