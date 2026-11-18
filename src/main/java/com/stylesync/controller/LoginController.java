package com.stylesync.controller;

import com.stylesync.model.User;
import com.stylesync.service.AuthService;
import com.stylesync.service.GoogleAuthService;
import com.stylesync.view.ImprovedDashboardView;
import com.stylesync.view.LoginView;
import com.stylesync.view.RegisterView;

import javax.swing.*;

/**
 * Controller for login operations
 */
public class LoginController {
    private final LoginView view;
    private final AuthService authService;

    public LoginController(LoginView view) {
        this.view = view;
        this.authService = new AuthService();
    }

    public void handleLogin() {
        String email = view.getEmail();
        String password = view.getPassword();

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            view.showError("Please fill in all fields");
            return;
        }

        try {
            // Try to login with email as username
            User user = authService.login(email, password);
            
            // Login successful
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                ImprovedDashboardView dashboard = new ImprovedDashboardView(user);
                dashboard.setVisible(true);
            });

        } catch (IllegalArgumentException e) {
            view.showError(e.getMessage());
        } catch (Exception e) {
            view.showError("An error occurred during login. Please try again.");
            e.printStackTrace();
        }
    }

    public void showRegisterView() {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            RegisterView registerView = new RegisterView();
            registerView.setVisible(true);
        });
    }

    public void handleGoogleLogin() {
        try {
            // Show loading message
            view.showInfo("Opening Google Sign-In in your browser...\nPlease complete the sign-in process.");
            
            // Authenticate with Google (opens browser)
            GoogleAuthService googleAuth = new GoogleAuthService();
            GoogleAuthService.GoogleUserInfo googleUser = 
                googleAuth.authenticateWithGoogle();
            
            // Login or create user account
            User user = authService.loginWithGoogle(
                googleUser.getEmail(), 
                googleUser.getName()
            );
            
            // Close login window and open dashboard
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                ImprovedDashboardView dashboard = new ImprovedDashboardView(user);
                dashboard.setVisible(true);
            });
            
        } catch (Exception e) {
            view.showError("Google Sign-In failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
