package com.stylesync.view;

import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.service.WardrobeService;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Modern Dashboard matching reference design
 */
public class ImprovedDashboardView extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private WardrobeService wardrobeService;
    
    // Views
    private WardrobeView wardrobeView;
    private OutfitView outfitView;
    private RecommendationView recommendationView;
    private AnalyticsView analyticsView;
    private ProfileView profileView;

    public ImprovedDashboardView(User user) {
        this.currentUser = user;
        this.wardrobeService = new WardrobeService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("StyleSync");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(250, 245, 255));

        // Left sidebar
        mainPanel.add(createModernSidebar(), BorderLayout.WEST);

        // Right content area with top bar
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(250, 245, 255));
        
        rightPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(250, 245, 255));

        // Initialize views
        wardrobeView = new WardrobeView(currentUser, this);
        outfitView = new OutfitView(currentUser, this);
        recommendationView = new RecommendationView(currentUser, this);
        analyticsView = new AnalyticsView(currentUser, this);
        profileView = new ProfileView(currentUser, this);

        contentPanel.add(createModernHomePanel(), "HOME");
        contentPanel.add(wardrobeView, "WARDROBE");
        contentPanel.add(outfitView, "OUTFITS");
        contentPanel.add(recommendationView, "RECOMMENDATIONS");
        contentPanel.add(analyticsView, "ANALYTICS");
        contentPanel.add(profileView, "PROFILE");

        rightPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createModernSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(45, 35, 75)); // Dark purple
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(new Color(45, 35, 75));
        logoPanel.setMaximumSize(new Dimension(220, 90));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoIcon = new JLabel("👗");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = new JLabel("StyleSync");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appName.setForeground(Color.WHITE);
        appName.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createVerticalStrut(5));
        logoPanel.add(appName);

        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(30));

        // Navigation items
        sidebar.add(createSidebarButton("🏠", "Dashboard", "HOME", true));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createSidebarButton("👔", "My Wardrobe", "WARDROBE", false));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createSidebarButton("✨", "Outfit Planner", "OUTFITS", false));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createSidebarButton("⭐", "Recommendations", "RECOMMENDATIONS", false));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createSidebarButton("📊", "Analytics", "ANALYTICS", false));
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(createSidebarButton("👤", "Profile & Settings", "PROFILE", false));

        sidebar.add(Box.createVerticalGlue());

        // Logout
        JButton logoutBtn = createSidebarButton("🚪", "Logout", "LOGOUT", false);
        logoutBtn.addActionListener(e -> handleLogout());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton createSidebarButton(String icon, String text, String action, boolean selected) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(190, 42));
        btn.setPreferredSize(new Dimension(190, 42));
        btn.setMargin(new Insets(0, 10, 0, 10));
        
        if (selected) {
            btn.setBackground(new Color(120, 80, 170)); // Highlighted purple
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
        } else {
            btn.setBackground(new Color(45, 35, 75));
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
        }

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!selected) {
                    btn.setBackground(new Color(60, 50, 90));
                    btn.setContentAreaFilled(true);
                    btn.setOpaque(true);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!selected) {
                    btn.setContentAreaFilled(false);
                    btn.setOpaque(false);
                }
            }
        });

        btn.addActionListener(e -> switchView(action));
        return btn;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        topBar.setPreferredSize(new Dimension(getWidth(), 70));

        // Search bar (left)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        ModernTextField searchField = new ModernTextField("🔍 Search anything...");
        searchField.setPreferredSize(new Dimension(350, 40));
        searchPanel.add(searchField);

        // Profile section (right)
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        profilePanel.setBackground(Color.WHITE);

        JLabel notifIcon = new JLabel("🔔");
        notifIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        notifIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userPanel.setBackground(Color.WHITE);
        userPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click listener to navigate to profile page
        userPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchView("PROFILE");
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                userPanel.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                userPanel.setBackground(Color.WHITE);
            }
        });

        JLabel profilePic = new JLabel("👤");
        profilePic.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        JLabel userName = new JLabel(currentUser.getFullName() + " ▼");
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setForeground(new Color(45, 35, 75));

        userPanel.add(profilePic);
        userPanel.add(userName);

        profilePanel.add(notifIcon);
        profilePanel.add(userPanel);

        topBar.add(searchPanel, BorderLayout.WEST);
        topBar.add(profilePanel, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createModernHomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 245, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Hero banner
        panel.add(createHeroBanner());
        panel.add(Box.createVerticalStrut(25));

        // Stats cards
        panel.add(createStatsCards());
        panel.add(Box.createVerticalStrut(25));

        // Content sections
        panel.add(createContentSections());

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane);
        return wrapper;
    }

    private JPanel createHeroBanner() {
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(150, 100, 200),
                    getWidth(), getHeight(), new Color(200, 150, 230)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        banner.setLayout(new BorderLayout());
        banner.setPreferredSize(new Dimension(900, 150));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        banner.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel welcome = new JLabel("Welcome back, " + currentUser.getFullName() + "!");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Your style, your way ✨");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tagline.setForeground(new Color(255, 255, 255, 200));

        ModernButton exploreBtn = new ModernButton("Explore Your Wardrobe →", false);
        exploreBtn.setBackground(Color.WHITE);
        exploreBtn.setForeground(new Color(120, 80, 170));
        exploreBtn.setPreferredSize(new Dimension(220, 40));
        exploreBtn.addActionListener(e -> switchView("WARDROBE"));

        textPanel.add(welcome);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(tagline);
        textPanel.add(Box.createVerticalStrut(15));
        textPanel.add(exploreBtn);

        banner.add(textPanel, BorderLayout.WEST);

        return banner;
    }

    private JPanel createStatsCards() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(new Color(250, 245, 255));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        List<ClothingItem> items = wardrobeService.getAllClothingItems(currentUser);
        int totalItems = items.size();
        long favorites = items.stream().filter(ClothingItem::isFavorite).count();

        statsPanel.add(createStatCard("👔", "Total Items", String.valueOf(totalItems), new Color(230, 220, 255)));
        statsPanel.add(createStatCard("⭐", "Favorite Outfits", String.valueOf(favorites), new Color(255, 220, 240)));
        statsPanel.add(createStatCard("📅", "Times Worn", "76", new Color(220, 255, 230)));
        statsPanel.add(createStatCard("❤️", "Style Score", "92%", new Color(255, 230, 230)));

        return statsPanel;
    }

    private JPanel createStatCard(String icon, String label, String value, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200, 50), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(new Color(45, 35, 75));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelText.setForeground(new Color(100, 100, 100));
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        card.add(labelText);

        return card;
    }

    private JPanel createContentSections() {
        JPanel sectionsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        sectionsPanel.setBackground(new Color(250, 245, 255));
        sectionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        // Recently Added section - links to Wardrobe
        JPanel recentlyAdded = createSection("Recently Added", "View All →", "WARDROBE");
        sectionsPanel.add(recentlyAdded);

        // Smart Recommendations section - links to Recommendations
        JPanel recommendations = createSection("Smart Recommendation", "View All →", "RECOMMENDATIONS");
        sectionsPanel.add(recommendations);

        return sectionsPanel;
    }

    private JPanel createSection(String title, String linkText, String targetView) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(Color.WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200, 80), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(45, 35, 75));

        JLabel link = new JLabel(linkText);
        link.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        link.setForeground(new Color(120, 80, 170));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click listener to navigate to the target view
        link.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchView(targetView);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                link.setForeground(new Color(150, 100, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                link.setForeground(new Color(120, 80, 170));
            }
        });

        header.add(titleLabel, BorderLayout.WEST);
        header.add(link, BorderLayout.EAST);

        // Content placeholder
        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setPreferredSize(new Dimension(400, 300));
        
        JLabel placeholder = new JLabel("Content will appear here");
        placeholder.setForeground(Color.GRAY);
        content.add(placeholder);

        section.add(header, BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);

        return section;
    }

    public void switchView(String viewName) {
        if (viewName.equals("LOGOUT")) {
            handleLogout();
        } else {
            cardLayout.show(contentPanel, viewName);
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            });
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
