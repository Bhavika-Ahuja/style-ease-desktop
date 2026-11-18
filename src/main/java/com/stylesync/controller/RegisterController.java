package com.stylesync.controller;

import com.stylesync.model.User;
import com.stylesync.service.AuthService;
import com.stylesync.view.ImprovedDashboardView;
import com.stylesync.view.LoginView;
import com.stylesync.view.RegisterView;

import javax.swing.*;

/**
 * Controller for registration operations
 */
public class RegisterController {
    private final RegisterView view;
    private final AuthService authService;

    public RegisterController(RegisterView view) {
        this.view = view;
        this.authService = new AuthService();
    }

    public void handleRegister() {
        String fullName = view.getFullName();
        String username = view.getUsername();
        String email = view.getEmail();
        String password = view.getPassword();
        String confirmPassword = view.getConfirmPassword();

        // Validation
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            view.showError("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.showError("Passwords do not match");
            return;
        }

        if (password.length() < 6) {
            view.showError("Password must be at least 6 characters");
            return;
        }

        if (!email.contains("@")) {
            view.showError("Please enter a valid email address");
            return;
        }

        try {
            // Register user
            User user = authService.register(username, email, password, fullName);
            
            // Automatically login the user
            User loggedInUser = authService.login(username, password);
            
            // Show success message
            view.showSuccess("Account created successfully! Welcome to StyleSync!");
            
            // Go directly to dashboard
            showDashboard(loggedInUser);

        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("An error occurred during registration. Please try again.");
            e.printStackTrace();
        }
    }
    
    private void showDashboard(User user) {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            ImprovedDashboardView dashboard = new ImprovedDashboardView(user);
            dashboard.setVisible(true);
        });
    }

    public void showLoginView() {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
