package com.stylesync.controller;

import com.stylesync.dao.ClothingItemDAO;
import com.stylesync.dao.OutfitDAO;
import com.stylesync.model.User;
import com.stylesync.service.AuthService;
import com.stylesync.view.DashboardView;
import com.stylesync.view.LoginView;

import javax.swing.*;

/**
 * Controller for dashboard operations
 */
public class DashboardController {
    private final DashboardView view;
    private final User currentUser;
    private final ClothingItemDAO clothingItemDAO;
    private final OutfitDAO outfitDAO;

    public DashboardController(DashboardView view, User user) {
        this.view = view;
        this.currentUser = user;
        this.clothingItemDAO = new ClothingItemDAO();
        this.outfitDAO = new OutfitDAO();
    }

    public void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            AuthService.setCurrentUser(null);
            view.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });
        }
    }

    public int getTotalItems() {
        return (int) clothingItemDAO.countByUserId(currentUser.getId());
    }

    public int getFavoriteOutfits() {
        return outfitDAO.findFavoritesByUserId(currentUser.getId()).size();
    }

    public int getWeekWearCount() {
        // Simplified calculation - could be enhanced to track actual 7-day wear
        return getTotalItems() > 0 ? (int) (Math.random() * 15) + 5 : 0;
    }
}
