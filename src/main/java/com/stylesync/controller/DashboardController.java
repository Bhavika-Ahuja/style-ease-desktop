package com.stylesync.controller;

import com.stylesync.dao.ClothingItemDAO;
import com.stylesync.dao.OutfitDAO;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.service.AuthService;
import com.stylesync.view.DashboardView;
import com.stylesync.view.LoginView;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        // Calculate total wear count from all items
        List<ClothingItem> items = clothingItemDAO.findByUserId(currentUser.getId());
        return items.stream().mapToInt(ClothingItem::getWearCount).sum();
    }

    /**
     * Get recently added items (sorted by date, newest first)
     */
    public List<ClothingItem> getRecentItems(int limit) {
        List<ClothingItem> allItems = clothingItemDAO.findByUserId(currentUser.getId());
        return allItems.stream()
                .sorted(Comparator.comparing(ClothingItem::getAddedDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get recommended items (least worn items)
     */
    public List<ClothingItem> getRecommendedItems(int limit) {
        List<ClothingItem> allItems = clothingItemDAO.findByUserId(currentUser.getId());
        return allItems.stream()
                .sorted(Comparator.comparing(ClothingItem::getWearCount))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
