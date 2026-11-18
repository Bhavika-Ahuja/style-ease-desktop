package com.stylesync.view;

import com.stylesync.controller.WardrobeController;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.util.ImageUtil;
import com.stylesync.util.SampleDataLoader;
import com.stylesync.view.components.*;
import com.stylesync.view.dialog.AddItemDialog;
import com.stylesync.view.dialog.ItemDetailDialog;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Wardrobe view with grid display and filters using Collections Framework
 */
public class WardrobeView extends JPanel {
    private WardrobeController controller;
    private User currentUser;
    private JFrame parentView; // Changed to JFrame to work with both DashboardView and ImprovedDashboardView
    
    // Collections Framework - ArrayList for items
    private ArrayList<ClothingItem> displayedItems;
    
    private JPanel itemsGridPanel;
    private JComboBox<String> typeFilterCombo;
    private JComboBox<String> colorFilterCombo;
    private JComboBox<String> seasonFilterCombo;
    private JComboBox<String> occasionFilterCombo;
    private JComboBox<String> sortCombo;
    private ModernTextField searchField;

    public WardrobeView(User user, JFrame parent) {
        this.currentUser = user;
        this.parentView = parent;
        this.controller = new WardrobeController(this, user);
        this.displayedItems = new ArrayList<>();
        initializeUI();
        refreshItems();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleSyncColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top panel with search and filters
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

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(StyleSyncColors.BACKGROUND);

        JLabel titleLabel = new JLabel("My Wardrobe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(StyleSyncColors.BACKGROUND);

        ModernButton addButton = new ModernButton("+ Add Clothing");
        addButton.addActionListener(e -> showAddItemDialog());
        buttonPanel.add(addButton);
        
        // Add sample data button (for demo purposes)
        ModernButton sampleDataButton = new ModernButton("Load Sample Items", false);
        sampleDataButton.setBackground(new Color(100, 180, 100));
        sampleDataButton.addActionListener(e -> loadSampleData());
        buttonPanel.add(sampleDataButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Search and filter panel
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchFilterPanel.setBackground(StyleSyncColors.BACKGROUND);

        // Search field
        searchField = new ModernTextField("Search clothing...");
        searchField.setPreferredSize(new Dimension(250, 40));
        searchField.addActionListener(e -> applyFilters());
        searchFilterPanel.add(searchField);

        // Filters using Collections - Arrays.asList converted to combo box items
        String[] types = {"All Categories", "Top", "Bottom", "Dress", "Outerwear", "Shoes", "Accessories"};
        typeFilterCombo = createFilterCombo(types);
        searchFilterPanel.add(new JLabel("Type:"));
        searchFilterPanel.add(typeFilterCombo);

        String[] colors = {"All Colors", "Black", "White", "Red", "Blue", "Green", "Yellow", "Pink", "Purple", "Gray", "Brown", "Orange"};
        colorFilterCombo = createFilterCombo(colors);
        searchFilterPanel.add(new JLabel("Color:"));
        searchFilterPanel.add(colorFilterCombo);

        String[] seasons = {"All Seasons", "Spring", "Summer", "Fall", "Winter"};
        seasonFilterCombo = createFilterCombo(seasons);
        searchFilterPanel.add(new JLabel("Season:"));
        searchFilterPanel.add(seasonFilterCombo);

        String[] occasions = {"All Occasions", "Casual", "Formal", "Party", "Work", "Sport"};
        occasionFilterCombo = createFilterCombo(occasions);
        searchFilterPanel.add(new JLabel("Occasion:"));
        searchFilterPanel.add(occasionFilterCombo);

        // Sort combo - will use Comparator
        String[] sortOptions = {"Recently Added", "Name (A-Z)", "Name (Z-A)", "Most Worn", "Least Worn", "Oldest First"};
        sortCombo = createFilterCombo(sortOptions);
        searchFilterPanel.add(new JLabel("Sort by:"));
        searchFilterPanel.add(sortCombo);

        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchFilterPanel, BorderLayout.SOUTH);

        return topPanel;
    }

    private JComboBox<String> createFilterCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setPreferredSize(new Dimension(140, 35));
        combo.setBackground(Color.WHITE);
        combo.addActionListener(e -> applyFilters());
        return combo;
    }

    private JPanel createGridPanel() {
        itemsGridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        itemsGridPanel.setBackground(StyleSyncColors.BACKGROUND);
        itemsGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return itemsGridPanel;
    }

    public void refreshItems() {
        displayedItems.clear();
        displayedItems.addAll(controller.getAllItems());
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText().trim();
        String type = (String) typeFilterCombo.getSelectedItem();
        String color = (String) colorFilterCombo.getSelectedItem();
        String season = (String) seasonFilterCombo.getSelectedItem();
        String occasion = (String) occasionFilterCombo.getSelectedItem();
        String sortBy = (String) sortCombo.getSelectedItem();

        // Convert "All..." selections to null for filtering
        if (type.startsWith("All")) type = null;
        if (color.startsWith("All")) color = null;
        if (season.startsWith("All")) season = null;
        if (occasion.startsWith("All")) occasion = null;

        // Get filtered items - using ArrayList
        ArrayList<ClothingItem> filteredItems = controller.searchAndFilterItems(
                searchText.isEmpty() ? null : searchText, type, color, season, occasion);

        // Sort using Comparator - Collections Framework
        filteredItems = controller.sortItems(filteredItems, sortBy);

        displayItems(filteredItems);
    }

    private void displayItems(ArrayList<ClothingItem> items) {
        itemsGridPanel.removeAll();

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items found. Add some clothing to your wardrobe!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            itemsGridPanel.setLayout(new BorderLayout());
            itemsGridPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            itemsGridPanel.setLayout(new GridLayout(0, 4, 20, 20));
            for (ClothingItem item : items) {
                itemsGridPanel.add(createItemCard(item));
            }
        }

        itemsGridPanel.revalidate();
        itemsGridPanel.repaint();
    }

    private RoundedPanel createItemCard(ClothingItem item) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 280));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(220, 200));

        try {
            ImageIcon icon = ImageUtil.getScaledImageIcon(item.getImagePath(), 200, 180);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholderLabel = new JLabel("No Image");
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            placeholderLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            imagePanel.add(placeholderLabel, BorderLayout.CENTER);
        }

        // Favorite icon overlay
        JPanel overlayPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        overlayPanel.setOpaque(false);
        JLabel favoriteIcon = new JLabel(item.isFavorite() ? "❤️" : "🤍");
        favoriteIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        favoriteIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoriteIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                evt.consume();
                controller.toggleFavorite(item);
                refreshItems();
            }
        });
        overlayPanel.add(favoriteIcon);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(220, 200));
        imagePanel.setBounds(0, 0, 220, 200);
        overlayPanel.setBounds(0, 0, 220, 200);
        layeredPane.add(imagePanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel typeLabel = new JLabel(item.getType() + " • " + item.getColor());
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        JLabel wearLabel = new JLabel("Worn " + item.getWearCount() + " times");
        wearLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        wearLabel.setForeground(StyleSyncColors.TEXT_LIGHT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(typeLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(wearLabel);

        card.add(layeredPane, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        // Click to view details
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showItemDetails(item);
            }
        });

        return card;
    }

    private void showAddItemDialog() {
        AddItemDialog dialog = new AddItemDialog(parentView, currentUser, this);
        dialog.setVisible(true);
    }

    private void showItemDetails(ClothingItem item) {
        ItemDetailDialog dialog = new ItemDetailDialog(parentView, item, this);
        dialog.setVisible(true);
    }

    public void showFavorites() {
        // Clear all filters and show only favorites
        ArrayList<ClothingItem> favorites = new ArrayList<>(controller.getFavoriteItems());
        displayItems(favorites);
    }

    public WardrobeController getController() {
        return controller;
    }
    
    private void loadSampleData() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "This will add 10 sample clothing items to your wardrobe.\nContinue?",
                "Load Sample Data",
                JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                SampleDataLoader loader = new SampleDataLoader();
                loader.loadSampleItems(currentUser);
                JOptionPane.showMessageDialog(this,
                        "Sample items loaded successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshItems();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error loading sample data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
