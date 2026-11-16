package com.stylesync.controller;

import com.stylesync.model.User;
import com.stylesync.service.AuthService;
import com.stylesync.view.DashboardView;
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
                DashboardView dashboard = new DashboardView(user);
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
}
