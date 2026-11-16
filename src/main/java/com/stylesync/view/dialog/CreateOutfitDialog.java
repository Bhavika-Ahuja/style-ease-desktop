package com.stylesync.view.dialog;

import com.stylesync.controller.WardrobeController;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.util.ImageUtil;
import com.stylesync.view.OutfitView;
import com.stylesync.view.StyleSyncColors;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Dialog for creating new outfits - uses ArrayList and HashSet (Collections Framework)
 */
public class CreateOutfitDialog extends JDialog {
    private User currentUser;
    private OutfitView parentView;
    private WardrobeController wardrobeController;
    
    // Collections Framework - HashSet for selected items (no duplicates)
    private HashSet<ClothingItem> selectedItems;
    
    // Collections Framework - ArrayList for all wardrobe items
    private ArrayList<ClothingItem> availableItems;
    
    private ModernTextField nameField;
    private JComboBox<String> occasionCombo;
    private JComboBox<String> seasonCombo;
    private JTextArea notesArea;
    private JPanel selectedItemsPanel;
    private JPanel availableItemsPanel;

    public CreateOutfitDialog(Frame parent, User user, OutfitView outfitView) {
        super(parent, "Create Outfit", true);
        this.currentUser = user;
        this.parentView = outfitView;
        this.wardrobeController = new WardrobeController(null, user);
        this.selectedItems = new HashSet<>(); // HashSet - no duplicates
        this.availableItems = new ArrayList<>();
        initializeUI();
        loadAvailableItems();
    }

    private void initializeUI() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Title
        JLabel titleLabel = new JLabel("Create Outfit");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JScrollPane scrollPane = new JScrollPane(createFormPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Name field
        panel.add(createLabel("Outfit Name *"));
        nameField = new ModernTextField("e.g., Summer Office Look");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(15));

        // Occasion and season
        JPanel comboPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        comboPanel.setBackground(Color.WHITE);
        comboPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel occasionPanel = new JPanel();
        occasionPanel.setLayout(new BoxLayout(occasionPanel, BoxLayout.Y_AXIS));
        occasionPanel.setBackground(Color.WHITE);
        occasionPanel.add(createLabel("Occasion *"));
        String[] occasions = {"Casual", "Formal", "Party", "Work", "Sport"};
        occasionCombo = new JComboBox<>(occasions);
        occasionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        occasionCombo.setBackground(Color.WHITE);
        occasionPanel.add(occasionCombo);

        JPanel seasonPanel = new JPanel();
        seasonPanel.setLayout(new BoxLayout(seasonPanel, BoxLayout.Y_AXIS));
        seasonPanel.setBackground(Color.WHITE);
        seasonPanel.add(createLabel("Season *"));
        String[] seasons = {"Spring", "Summer", "Fall", "Winter"};
        seasonCombo = new JComboBox<>(seasons);
        seasonCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        seasonCombo.setBackground(Color.WHITE);
        seasonPanel.add(seasonCombo);

        comboPanel.add(occasionPanel);
        comboPanel.add(seasonPanel);
        panel.add(comboPanel);
        panel.add(Box.createVerticalStrut(15));

        // Selected items section
        panel.add(createLabel("Select Items (" + selectedItems.size() + " selected)"));
        panel.add(Box.createVerticalStrut(10));
        
        selectedItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        selectedItemsPanel.setBackground(StyleSyncColors.BACKGROUND);
        selectedItemsPanel.setBorder(BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1));
        selectedItemsPanel.setPreferredSize(new Dimension(800, 120));
        
        JScrollPane selectedScroll = new JScrollPane(selectedItemsPanel);
        selectedScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panel.add(selectedScroll);
        panel.add(Box.createVerticalStrut(15));

        // Available items section
        panel.add(createLabel("Your Wardrobe"));
        panel.add(Box.createVerticalStrut(10));
        
        availableItemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        availableItemsPanel.setBackground(Color.WHITE);
        
        JScrollPane availableScroll = new JScrollPane(availableItemsPanel);
        availableScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        availableScroll.setBorder(BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1));
        panel.add(availableScroll);
        panel.add(Box.createVerticalStrut(15));

        // Notes
        panel.add(createLabel("Notes"));
        notesArea = new JTextArea(2, 20);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.add(notesScroll);

        return panel;
    }

    private void loadAvailableItems() {
        availableItems.clear();
        availableItems.addAll(wardrobeController.getAllItems());
        displayAvailableItems();
    }

    private void displayAvailableItems() {
        availableItemsPanel.removeAll();

        // Use ArrayList - iterate through collection
        for (ClothingItem item : availableItems) {
            availableItemsPanel.add(createItemThumbnail(item, false));
        }

        availableItemsPanel.revalidate();
        availableItemsPanel.repaint();
    }

    private void displaySelectedItems() {
        selectedItemsPanel.removeAll();

        // Use HashSet - iterate through collection
        for (ClothingItem item : selectedItems) {
            selectedItemsPanel.add(createItemThumbnail(item, true));
        }

        // Update display
        selectedItemsPanel.revalidate();
        selectedItemsPanel.repaint();
    }

    private JPanel createItemThumbnail(ClothingItem item, boolean isSelected) {
        JPanel thumbnail = new JPanel(new BorderLayout());
        thumbnail.setBackground(Color.WHITE);
        thumbnail.setPreferredSize(new Dimension(90, 110));
        thumbnail.setBorder(BorderFactory.createLineBorder(
                isSelected ? StyleSyncColors.PRIMARY : StyleSyncColors.BORDER, 2));
        thumbnail.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = ImageUtil.getScaledImageIcon(item.getImagePath(), 85, 80);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            thumbnail.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholder = new JLabel("No Img");
            placeholder.setHorizontalAlignment(SwingConstants.CENTER);
            placeholder.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            placeholder.setForeground(StyleSyncColors.TEXT_SECONDARY);
            thumbnail.add(placeholder, BorderLayout.CENTER);
        }

        JLabel nameLabel = new JLabel(item.getName().length() > 10 ? 
                item.getName().substring(0, 10) + "..." : item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thumbnail.add(nameLabel, BorderLayout.SOUTH);

        // Click to toggle selection - using HashSet add/remove
        thumbnail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (isSelected) {
                    selectedItems.remove(item); // HashSet remove
                } else {
                    selectedItems.add(item); // HashSet add (no duplicates)
                }
                displaySelectedItems();
            }
        });

        return thumbnail;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(StyleSyncColors.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panel.setBackground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(e -> dispose());

        ModernButton saveButton = new ModernButton("Create Outfit");
        saveButton.setPreferredSize(new Dimension(140, 40));
        saveButton.addActionListener(e -> saveOutfit());

        panel.add(cancelButton);
        panel.add(saveButton);

        return panel;
    }

    private void saveOutfit() {
        // Validation
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter outfit name", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one item", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Convert HashSet to ArrayList for service call
            ArrayList<ClothingItem> itemsList = new ArrayList<>(selectedItems);
            
            parentView.getController().createOutfit(
                    nameField.getText().trim(),
                    itemsList,
                    (String) occasionCombo.getSelectedItem(),
                    (String) seasonCombo.getSelectedItem(),
                    notesArea.getText().trim()
            );

            JOptionPane.showMessageDialog(this, "Outfit created successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            parentView.refreshOutfits();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating outfit: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
