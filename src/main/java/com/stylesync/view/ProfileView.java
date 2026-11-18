package com.stylesync.view;

import com.stylesync.model.User;
import com.stylesync.service.AuthService;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Profile & Settings View matching reference design
 */
public class ProfileView extends JPanel {
    private User currentUser;
    private JFrame parentView; // Changed to JFrame to work with both DashboardView and ImprovedDashboardView
    
    private ModernTextField fullNameField;
    private ModernTextField usernameField;
    private ModernTextField emailField;
    private JComboBox<String> styleCombo;

    public ProfileView(User user, JFrame parent) {
        this.currentUser = user;
        this.parentView = parent;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleSyncColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(StyleSyncColors.BACKGROUND);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Profile & Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        add(titlePanel, BorderLayout.NORTH);

        // Main content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        tabbedPane.addTab("Profile", createProfileTab());
        tabbedPane.addTab("Preferences", createPreferencesTab());
        tabbedPane.addTab("Security", createSecurityTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProfileTab() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Left side - Profile picture and basic info
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // Profile picture
        JLabel profilePic = new JLabel("👤");
        profilePic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        profilePic.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePic.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(StyleSyncColors.PRIMARY, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        ModernButton changePhotoBtn = new ModernButton("Change Photo", false);
        changePhotoBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePhotoBtn.setPreferredSize(new Dimension(150, 35));

        leftPanel.add(profilePic);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(changePhotoBtn);

        // Right side - Form fields
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;

        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel fullNameLabel = new JLabel("Full Name");
        fullNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightPanel.add(fullNameLabel, gbc);

        gbc.gridy = 1;
        fullNameField = new ModernTextField(currentUser.getFullName());
        fullNameField.setPreferredSize(new Dimension(400, 45));
        rightPanel.add(fullNameField, gbc);

        // Username
        gbc.gridy = 2;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightPanel.add(usernameLabel, gbc);

        gbc.gridy = 3;
        usernameField = new ModernTextField(currentUser.getUsername());
        usernameField.setPreferredSize(new Dimension(400, 45));
        rightPanel.add(usernameField, gbc);

        // Email
        gbc.gridy = 4;
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rightPanel.add(emailLabel, gbc);

        gbc.gridy = 5;
        emailField = new ModernTextField(currentUser.getEmail());
        emailField.setPreferredSize(new Dimension(400, 45));
        emailField.setEditable(false); // Email can't be changed
        rightPanel.add(emailField, gbc);

        // Buttons
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        ModernButton cancelBtn = new ModernButton("Cancel", false);
        cancelBtn.setBackground(Color.LIGHT_GRAY);
        cancelBtn.setPreferredSize(new Dimension(120, 40));

        ModernButton saveBtn = new ModernButton("Save Changes");
        saveBtn.setPreferredSize(new Dimension(150, 40));
        saveBtn.addActionListener(e -> saveProfileChanges());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        rightPanel.add(buttonPanel, gbc);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPreferencesTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        // Preferred Style
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel styleLabel = new JLabel("Preferred Style");
        styleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(styleLabel, gbc);

        gbc.gridy = 1;
        String[] styles = {"Casual", "Formal", "Sporty", "Elegant", "Trendy", "Classic"};
        styleCombo = new JComboBox<>(styles);
        styleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        styleCombo.setPreferredSize(new Dimension(300, 40));
        panel.add(styleCombo, gbc);

        // Notifications
        gbc.gridy = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        JLabel notifLabel = new JLabel("Notifications");
        notifLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(notifLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 10, 5, 10);
        JCheckBox emailNotif = new JCheckBox("Email notifications");
        emailNotif.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailNotif.setBackground(Color.WHITE);
        panel.add(emailNotif, gbc);

        gbc.gridy = 4;
        JCheckBox weatherNotif = new JCheckBox("Weather-based recommendations");
        weatherNotif.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        weatherNotif.setBackground(Color.WHITE);
        weatherNotif.setSelected(true);
        panel.add(weatherNotif, gbc);

        // Filler
        gbc.gridy = 5;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel createSecurityTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        // Change Password Section
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel passwordLabel = new JLabel("Change Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(passwordLabel, gbc);

        gbc.gridy = 1;
        ModernPasswordField currentPass = new ModernPasswordField("Current Password");
        currentPass.setPreferredSize(new Dimension(300, 45));
        panel.add(currentPass, gbc);

        gbc.gridy = 2;
        ModernPasswordField newPass = new ModernPasswordField("New Password");
        newPass.setPreferredSize(new Dimension(300, 45));
        panel.add(newPass, gbc);

        gbc.gridy = 3;
        ModernPasswordField confirmPass = new ModernPasswordField("Confirm New Password");
        confirmPass.setPreferredSize(new Dimension(300, 45));
        panel.add(confirmPass, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 10, 10, 10);
        ModernButton changePassBtn = new ModernButton("Update Password");
        changePassBtn.setPreferredSize(new Dimension(180, 40));
        panel.add(changePassBtn, gbc);

        // Account Actions
        gbc.gridy = 5;
        gbc.insets = new Insets(50, 10, 10, 10);
        JLabel accountLabel = new JLabel("Account Actions");
        accountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(accountLabel, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 10, 10);
        JButton deleteBtn = new JButton("Delete Account");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 53, 69));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(deleteBtn, gbc);

        // Filler
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private void saveProfileChanges() {
        // Update user information
        String newFullName = fullNameField.getText().trim();
        String newUsername = usernameField.getText().trim();

        if (newFullName.isEmpty() || newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Name and username cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentUser.setFullName(newFullName);
        currentUser.setUsername(newUsername);

        // In a real app, you'd save to database here
        JOptionPane.showMessageDialog(this, 
            "Profile updated successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
