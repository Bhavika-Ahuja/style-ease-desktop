package com.stylesync.service;

import com.stylesync.dao.UserDAO;
import com.stylesync.model.User;
import com.stylesync.util.PasswordUtil;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Service for authentication operations
 */
public class AuthService {
    private final UserDAO userDAO;
    private static User currentUser;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User register(String username, String email, String password, String fullName) {
        // Validate input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // Check if username or email already exists
        if (userDAO.usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Hash password and create user
        String passwordHash = PasswordUtil.hashPassword(password);
        User user = new User(username, email, passwordHash, fullName);
        return userDAO.save(user);
    }

    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Update last login
        userDAO.updateLastLogin(user.getId());
        user.setLastLogin(new Date());

        currentUser = user;
        return user;
    }

    public void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
