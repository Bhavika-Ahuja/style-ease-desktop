package com.stylesync.view;

import com.stylesync.controller.DashboardController;
import com.stylesync.model.User;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard with sidebar navigation
 */
public class DashboardView extends JFrame {
    private DashboardController controller;
    private User currentUser;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private CardLayout cardLayout;
    
    // Views
    private WardrobeView wardrobeView;
    private OutfitView outfitView;
    private RecommendationView recommendationView;
    private AnalyticsView analyticsView;
    private ProfileView profileView;

    public DashboardView(User user) {
        this.currentUser = user;
        this.controller = new DashboardController(this, user);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("StyleSync - Dashboard");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);  // Allow resizing and fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Start maximized

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleSyncColors.BACKGROUND);

        // Sidebar
        sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(StyleSyncColors.BACKGROUND);

        // Initialize views
        wardrobeView = new WardrobeView(currentUser, this);
        outfitView = new OutfitView(currentUser, this);
        recommendationView = new RecommendationView(currentUser, this);
        analyticsView = new AnalyticsView(currentUser, this);
        profileView = new ProfileView(currentUser, this);

        contentPanel.add(createHomePanel(), "HOME");
        contentPanel.add(wardrobeView, "WARDROBE");
        contentPanel.add(outfitView, "OUTFITS");
        contentPanel.add(recommendationView, "RECOMMENDATIONS");
        contentPanel.add(analyticsView, "ANALYTICS");
        contentPanel.add(profileView, "PROFILE");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(StyleSyncColors.SIDEBAR_DARK);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Logo section
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        logoPanel.setBackground(StyleSyncColors.SIDEBAR_DARK);
        logoPanel.setMaximumSize(new Dimension(250, 80));

        JLabel logoIcon = new JLabel("👗");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JLabel appName = new JLabel("StyleSync");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appName.setForeground(Color.WHITE);

        logoPanel.add(logoIcon);
        logoPanel.add(appName);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(30));

        // Navigation items
        sidebar.add(createNavItem("🏠", "Dashboard", "HOME"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createNavItem("👔", "My Wardrobe", "WARDROBE"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createNavItem("✨", "Outfit Planner", "OUTFITS"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createNavItem("🎯", "Smart Recommendations", "RECOMMENDATIONS"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createNavItem("📊", "Wear History", "ANALYTICS"));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createNavItem("👤", "Profile & Settings", "PROFILE"));

        sidebar.add(Box.createVerticalGlue());

        // Logout button
        JButton logoutBtn = createNavItem("🚪", "Logout", "LOGOUT");
        logoutBtn.addActionListener(e -> controller.handleLogout());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton createNavItem(String icon, String text, String action) {
        JButton button = new JButton(" " + icon + "   " + text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(StyleSyncColors.SIDEBAR_DARK);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(220, 45));
        button.setPreferredSize(new Dimension(220, 45));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(StyleSyncColors.SIDEBAR_ITEM_HOVER);
                button.setContentAreaFilled(true);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
            }
        });

        button.addActionListener(e -> switchView(action));

        return button;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleSyncColors.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Header with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = StyleSyncColors.createHorizontalGradient(getWidth());
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 150));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome back, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel taglineLabel = new JLabel("Your style, your way");
        taglineLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        taglineLabel.setForeground(new Color(255, 255, 255, 230));

        ModernButton exploreButton = new ModernButton("Let's Style!", false);
        exploreButton.setBackground(new Color(255, 255, 255, 40));
        exploreButton.setPreferredSize(new Dimension(150, 40));
        exploreButton.addActionListener(e -> switchView("WARDROBE"));

        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(taglineLabel);
        textPanel.add(Box.createVerticalStrut(15));
        textPanel.add(exploreButton);
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerContent.add(textPanel, BorderLayout.WEST);
        headerPanel.add(headerContent);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Main content area with stats and sections
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(StyleSyncColors.BACKGROUND);

        // Stats cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBackground(StyleSyncColors.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        statsPanel.add(createStatCard("Total Items", 
                String.valueOf(controller.getTotalItems()), 
                StyleSyncColors.PRIMARY));
        statsPanel.add(createStatCard("Favorite Outfits", 
                String.valueOf(controller.getFavoriteOutfits()), 
                StyleSyncColors.SECONDARY));
        statsPanel.add(createStatCard("Times Worn", 
                String.valueOf(controller.getWeekWearCount()), 
                StyleSyncColors.SUCCESS));
        statsPanel.add(createStatCard("Style Score", 
                "92", 
                StyleSyncColors.INFO));

        mainContent.add(statsPanel, BorderLayout.NORTH);

        // Recently Added and Smart Recommendation sections
        JPanel sectionsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        sectionsPanel.setBackground(StyleSyncColors.BACKGROUND);

        sectionsPanel.add(createRecentlyAddedSection());
        sectionsPanel.add(createSmartRecommendationSection());

        mainContent.add(sectionsPanel, BorderLayout.CENTER);
        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRecentlyAddedSection() {
        RoundedPanel section = new RoundedPanel(15);
        section.setLayout(new BorderLayout());
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Recently Added");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel viewAllLabel = new JLabel("View All →");
        viewAllLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        viewAllLabel.setForeground(StyleSyncColors.PRIMARY);
        viewAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchView("WARDROBE");
            }
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(viewAllLabel, BorderLayout.EAST);

        // Items panel
        JPanel itemsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Get recent items
        java.util.List<com.stylesync.model.ClothingItem> recentItems = controller.getRecentItems(3);
        
        if (recentItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items yet. Add some clothing!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            itemsPanel.setLayout(new BorderLayout());
            itemsPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            for (com.stylesync.model.ClothingItem item : recentItems) {
                itemsPanel.add(createMiniItemCard(item));
            }
        }

        section.add(headerPanel, BorderLayout.NORTH);
        section.add(itemsPanel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createSmartRecommendationSection() {
        RoundedPanel section = new RoundedPanel(15);
        section.setLayout(new BorderLayout());
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Smart Recommendation");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel viewAllLabel = new JLabel("View All →");
        viewAllLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        viewAllLabel.setForeground(StyleSyncColors.PRIMARY);
        viewAllLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAllLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchView("RECOMMENDATIONS");
            }
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(viewAllLabel, BorderLayout.EAST);

        // Recommendation content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Get recommended items (least worn or favorites)
        java.util.List<com.stylesync.model.ClothingItem> recommended = controller.getRecommendedItems(3);

        if (recommended.isEmpty()) {
            JLabel emptyLabel = new JLabel("Add items to get smart recommendations!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(emptyLabel);
        } else {
            JLabel tipLabel = new JLabel("💡 Try wearing these items:");
            tipLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tipLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            contentPanel.add(tipLabel);
            contentPanel.add(Box.createVerticalStrut(10));

            for (com.stylesync.model.ClothingItem item : recommended) {
                contentPanel.add(createRecommendationItem(item));
                contentPanel.add(Box.createVerticalStrut(8));
            }
        }

        section.add(headerPanel, BorderLayout.NORTH);
        section.add(contentPanel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createMiniItemCard(com.stylesync.model.ClothingItem item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(StyleSyncColors.BACKGROUND);
        card.setBorder(BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1));

        // Image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(Color.WHITE);
        imageLabel.setOpaque(true);

        try {
            com.stylesync.util.ImageUtil.getScaledImageIcon(item.getImagePath(), 150, 140);
            ImageIcon icon = com.stylesync.util.ImageUtil.getScaledImageIcon(item.getImagePath(), 150, 140);
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            imageLabel.setText("No Image");
            imageLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
        }

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel typeLabel = new JLabel(item.getType());
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        typeLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        infoPanel.add(nameLabel);
        infoPanel.add(typeLabel);

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchView("WARDROBE");
            }
        });

        return card;
    }

    private JPanel createRecommendationItem(com.stylesync.model.ClothingItem item) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Mini image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(50, 50));
        imageLabel.setBackground(StyleSyncColors.BACKGROUND);
        imageLabel.setOpaque(true);
        imageLabel.setBorder(BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1));

        try {
            ImageIcon icon = com.stylesync.util.ImageUtil.getScaledImageIcon(item.getImagePath(), 50, 50);
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            imageLabel.setText("?");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        // Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel detailLabel = new JLabel("Worn " + item.getWearCount() + " times");
        detailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        detailLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        infoPanel.add(nameLabel);
        infoPanel.add(detailLabel);

        panel.add(imageLabel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);

        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchView("WARDROBE");
            }
        });

        return panel;
    }

    private RoundedPanel createStatCard(String title, String value, Color color) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    public void switchView(String viewName) {
        if (viewName.equals("LOGOUT")) {
            return; // Handled by controller
        }
        
        if (viewName.equals("FAVORITES")) {
            wardrobeView.showFavorites();
            viewName = "WARDROBE";
        }
        
        if (viewName.equals("SETTINGS")) {
            // TODO: Implement settings view
            JOptionPane.showMessageDialog(this, "Settings coming soon!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        cardLayout.show(contentPanel, viewName);
        
        // Refresh view if needed
        if (viewName.equals("WARDROBE")) {
            wardrobeView.refreshItems();
        } else if (viewName.equals("OUTFITS")) {
            outfitView.refreshOutfits();
        } else if (viewName.equals("ANALYTICS")) {
            analyticsView.refreshStats();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void refreshDashboard() {
        // Refresh all views
        wardrobeView.refreshItems();
        outfitView.refreshOutfits();
        analyticsView.refreshStats();
    }
}
