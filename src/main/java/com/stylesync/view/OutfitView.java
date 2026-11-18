package com.stylesync.view;

import com.stylesync.controller.OutfitController;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.Outfit;
import com.stylesync.model.User;
import com.stylesync.util.ImageUtil;
import com.stylesync.view.components.*;
import com.stylesync.view.dialog.CreateOutfitDialog;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Outfit view for creating and managing outfits - using Collections Framework
 */
public class OutfitView extends JPanel {
    private OutfitController controller;
    private User currentUser;
    private JFrame parentView; // Changed to JFrame to work with both DashboardView and ImprovedDashboardView
    
    // Collections Framework - ArrayList for outfits
    private ArrayList<Outfit> displayedOutfits;
    
    private JPanel outfitsGridPanel;

    public OutfitView(User user, JFrame parent) {
        this.currentUser = user;
        this.parentView = parent;
        this.controller = new OutfitController(this, user);
        this.displayedOutfits = new ArrayList<>();
        initializeUI();
        refreshOutfits();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleSyncColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top panel
        add(createTopPanel(), BorderLayout.NORTH);

        // Center panel with grid
        JScrollPane scrollPane = new JScrollPane(createGridPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(StyleSyncColors.BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Outfit Planner");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(StyleSyncColors.BACKGROUND);

        ModernButton createButton = new ModernButton("+ Create Outfit");
        createButton.addActionListener(e -> showCreateOutfitDialog());
        buttonPanel.add(createButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createGridPanel() {
        outfitsGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        outfitsGridPanel.setBackground(StyleSyncColors.BACKGROUND);
        outfitsGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return outfitsGridPanel;
    }

    public void refreshOutfits() {
        displayedOutfits.clear();
        displayedOutfits.addAll(controller.getAllOutfits());
        displayOutfits();
    }

    private void displayOutfits() {
        outfitsGridPanel.removeAll();

        if (displayedOutfits.isEmpty()) {
            JLabel emptyLabel = new JLabel("No outfits yet. Create your first outfit!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            outfitsGridPanel.setLayout(new BorderLayout());
            outfitsGridPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            outfitsGridPanel.setLayout(new GridLayout(0, 3, 20, 20));
            for (Outfit outfit : displayedOutfits) {
                outfitsGridPanel.add(createOutfitCard(outfit));
            }
        }

        outfitsGridPanel.revalidate();
        outfitsGridPanel.repaint();
    }

    private RoundedPanel createOutfitCard(Outfit outfit) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(320, 380));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Get outfit items - using ArrayList
        ArrayList<ClothingItem> items = controller.getOutfitItems(outfit);

        // Images panel - show up to 4 items
        JPanel imagesPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        imagesPanel.setBackground(Color.WHITE);
        imagesPanel.setPreferredSize(new Dimension(320, 280));
        imagesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < Math.min(4, items.size()); i++) {
            ClothingItem item = items.get(i);
            try {
                ImageIcon icon = ImageUtil.getScaledImageIcon(item.getImagePath(), 140, 130);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagesPanel.add(imageLabel);
            } catch (Exception e) {
                JLabel placeholder = new JLabel("No Image");
                placeholder.setHorizontalAlignment(SwingConstants.CENTER);
                placeholder.setForeground(StyleSyncColors.TEXT_SECONDARY);
                imagesPanel.add(placeholder);
            }
        }

        // Fill remaining spaces if less than 4 items
        for (int i = items.size(); i < 4; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(StyleSyncColors.BACKGROUND);
            imagesPanel.add(emptyPanel);
        }

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(outfit.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel favoriteIcon = new JLabel(outfit.isFavorite() ? "❤️" : "🤍");
        favoriteIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        favoriteIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoriteIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                evt.consume();
                controller.toggleFavorite(outfit);
                refreshOutfits();
            }
        });

        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(favoriteIcon, BorderLayout.EAST);

        JLabel detailsLabel = new JLabel(items.size() + " items • " + outfit.getOccasion());
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailsLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        JLabel wearLabel = new JLabel("Worn " + outfit.getWearCount() + " times");
        wearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        wearLabel.setForeground(StyleSyncColors.TEXT_LIGHT);

        infoPanel.add(namePanel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(detailsLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(wearLabel);

        card.add(imagesPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        // Click to view details
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showOutfitOptions(outfit, items);
            }
        });

        return card;
    }

    private void showOutfitOptions(Outfit outfit, ArrayList<ClothingItem> items) {
        String[] options = {"Mark as Worn", "Delete", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Outfit: " + outfit.getName() + "\n" + items.size() + " items",
                "Outfit Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            controller.markAsWorn(outfit);
            JOptionPane.showMessageDialog(this, "Outfit marked as worn!", "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshOutfits();
        } else if (choice == 1) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this outfit?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteOutfit(outfit);
                refreshOutfits();
            }
        }
    }

    private void showCreateOutfitDialog() {
        CreateOutfitDialog dialog = new CreateOutfitDialog(parentView, currentUser, this);
        dialog.setVisible(true);
    }

    public OutfitController getController() {
        return controller;
    }
}
