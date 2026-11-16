package com.stylesync.view;

import com.stylesync.controller.LoginController;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen with modern design
 */
public class LoginView extends JFrame {
    private LoginController controller;
    private ModernTextField emailField;
    private ModernPasswordField passwordField;
    private JCheckBox rememberMeCheckbox;

    public LoginView() {
        this.controller = new LoginController(this);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("StyleSync - Login");
        setSize(1000, 650);
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

        // Right panel - Login form
        JPanel rightPanel = createLoginPanel();

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

        // Logo
        JLabel logoIcon = new JLabel("👗");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        logoIcon.setForeground(Color.WHITE);
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("StyleSync");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 48));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("Your digital closet,");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline.setForeground(new Color(255, 255, 255, 230));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline2 = new JLabel("organized and intelligent");
        tagline2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tagline2.setForeground(new Color(255, 255, 255, 230));
        tagline2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Decorative image placeholder
        JLabel imageLabel = new JLabel("🛍️");
        imageLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        imageLabel.setForeground(new Color(255, 255, 255, 180));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(logoIcon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(appName);
        panel.add(Box.createVerticalStrut(20));
        panel.add(tagline);
        panel.add(tagline2);
        panel.add(Box.createVerticalStrut(40));
        panel.add(imageLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        // Welcome back title
        JLabel titleLabel = new JLabel("Welcome Back!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Sign in to continue your style journey");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 30, 20);
        panel.add(subtitleLabel, gbc);

        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email field
        emailField = new ModernTextField("Email Address");
        gbc.gridy = 2;
        panel.add(emailField, gbc);

        // Password field
        passwordField = new ModernPasswordField("Password");
        gbc.gridy = 3;
        panel.add(passwordField, gbc);

        // Remember me and forgot password
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setOpaque(false);

        rememberMeCheckbox = new JCheckBox("Remember Me");
        rememberMeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberMeCheckbox.setForeground(StyleSyncColors.TEXT_SECONDARY);
        rememberMeCheckbox.setOpaque(false);
        rememberMeCheckbox.setFocusPainted(false);

        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(StyleSyncColors.PRIMARY);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        optionsPanel.add(rememberMeCheckbox, BorderLayout.WEST);
        optionsPanel.add(forgotPasswordLabel, BorderLayout.EAST);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 20, 20, 20);
        panel.add(optionsPanel, gbc);

        // Login button
        ModernButton loginButton = new ModernButton("Login");
        loginButton.addActionListener(e -> controller.handleLogin());
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 20, 15, 20);
        panel.add(loginButton, gbc);

        // Divider
        JLabel dividerLabel = new JLabel("─────────  or  ─────────");
        dividerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dividerLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 20, 10, 20);
        panel.add(dividerLabel, gbc);

        // Google login button
        JButton googleButton = new JButton("Continue with Google");
        googleButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        googleButton.setForeground(StyleSyncColors.TEXT_PRIMARY);
        googleButton.setBackground(Color.WHITE);
        googleButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        googleButton.setFocusPainted(false);
        googleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        googleButton.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 20, 20, 20);
        panel.add(googleButton, gbc);

        // Sign up link
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        signupPanel.setOpaque(false);
        JLabel noAccountLabel = new JLabel("Don't have an account?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        JLabel signupLabel = new JLabel("<html><u>Sign Up</u></html>");
        signupLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signupLabel.setForeground(StyleSyncColors.PRIMARY);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                controller.showRegisterView();
            }
        });

        signupPanel.add(noAccountLabel);
        signupPanel.add(signupLabel);

        gbc.gridy = 8;
        gbc.insets = new Insets(10, 20, 20, 20);
        panel.add(signupPanel, gbc);

        return panel;
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isRememberMeSelected() {
        return rememberMeCheckbox.isSelected();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }
}
