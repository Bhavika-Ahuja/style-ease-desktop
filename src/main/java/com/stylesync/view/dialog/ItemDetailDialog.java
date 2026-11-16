package com.stylesync.view.dialog;

import com.stylesync.model.ClothingItem;
import com.stylesync.util.ImageUtil;
import com.stylesync.view.StyleSyncColors;
import com.stylesync.view.WardrobeView;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 * Dialog showing clothing item details with edit/delete options
 */
public class ItemDetailDialog extends JDialog {
    private ClothingItem item;
    private WardrobeView parentView;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

    public ItemDetailDialog(Frame parent, ClothingItem item, WardrobeView wardrobeView) {
        super(parent, "Item Details", true);
        this.item = item;
        this.parentView = wardrobeView;
        initializeUI();
    }

    private void initializeUI() {
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Content
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Image
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(StyleSyncColors.BACKGROUND);
        imagePanel.setBorder(BorderFactory.createLineBorder(StyleSyncColors.BORDER, 2));
        imagePanel.setMaximumSize(new Dimension(540, 350));
        imagePanel.setPreferredSize(new Dimension(540, 350));

        try {
            ImageIcon icon = ImageUtil.getScaledImageIcon(item.getImagePath(), 520, 330);
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagePanel.add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel placeholderLabel = new JLabel("Image not available");
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            placeholderLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            imagePanel.add(placeholderLabel, BorderLayout.CENTER);
        }

        panel.add(imagePanel);
        panel.add(Box.createVerticalStrut(25));

        // Name and favorite
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setBackground(Color.WHITE);
        namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel favoriteIcon = new JLabel(item.isFavorite() ? "❤️" : "🤍");
        favoriteIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        favoriteIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        favoriteIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parentView.getController().toggleFavorite(item);
                favoriteIcon.setText(item.isFavorite() ? "❤️" : "🤍");
            }
        });

        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(favoriteIcon, BorderLayout.EAST);
        panel.add(namePanel);
        panel.add(Box.createVerticalStrut(20));

        // Details
        panel.add(createDetailRow("Category:", item.getType()));
        panel.add(createDetailRow("Color:", item.getColor()));
        panel.add(createDetailRow("Seasons:", String.join(", ", item.getSeasons())));
        panel.add(createDetailRow("Occasions:", String.join(", ", item.getOccasions())));
        panel.add(createDetailRow("Styles:", String.join(", ", item.getStyles())));
        panel.add(createDetailRow("Added Date:", dateFormat.format(item.getAddedDate())));
        panel.add(createDetailRow("Wear Count:", String.valueOf(item.getWearCount())));
        
        if (item.getLastWornDate() != null) {
            panel.add(createDetailRow("Last Worn:", dateFormat.format(item.getLastWornDate())));
        }

        if (item.getNotes() != null && !item.getNotes().isEmpty()) {
            panel.add(Box.createVerticalStrut(15));
            JLabel notesTitle = new JLabel("Notes:");
            notesTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
            notesTitle.setForeground(StyleSyncColors.TEXT_PRIMARY);
            notesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(notesTitle);
            
            JTextArea notesArea = new JTextArea(item.getNotes());
            notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            notesArea.setForeground(StyleSyncColors.TEXT_SECONDARY);
            notesArea.setLineWrap(true);
            notesArea.setWrapStyleWord(true);
            notesArea.setEditable(false);
            notesArea.setBackground(StyleSyncColors.BACKGROUND);
            notesArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(notesArea);
        }

        return panel;
    }

    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel labelComp = new JLabel(label + " ");
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelComp.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel valueComp = new JLabel(value.isEmpty() ? "N/A" : value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueComp.setForeground(StyleSyncColors.TEXT_SECONDARY);

        row.add(labelComp);
        row.add(valueComp);

        return row;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, StyleSyncColors.BORDER));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deleteButton.setForeground(StyleSyncColors.ERROR);
        deleteButton.setBackground(Color.WHITE);
        deleteButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.ERROR, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteItem());

        ModernButton markWornButton = new ModernButton("Mark as Worn", false);
        markWornButton.setBackground(StyleSyncColors.SUCCESS);
        markWornButton.addActionListener(e -> markAsWorn());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        closeButton.setFocusPainted(false);
        closeButton.setBackground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        panel.add(deleteButton);
        panel.add(markWornButton);
        panel.add(closeButton);

        return panel;
    }

    private void markAsWorn() {
        parentView.getController().markAsWorn(item);
        JOptionPane.showMessageDialog(this, "Item marked as worn!", "Success", JOptionPane.INFORMATION_MESSAGE);
        parentView.refreshItems();
        dispose();
    }

    private void deleteItem() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this item?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            parentView.getController().deleteItem(item);
            JOptionPane.showMessageDialog(this, "Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentView.refreshItems();
            dispose();
        }
    }
}
