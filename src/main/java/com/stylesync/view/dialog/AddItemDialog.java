package com.stylesync.view.dialog;

import com.github.sarxos.webcam.Webcam;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.view.WardrobeView;
import com.stylesync.view.StyleSyncColors;
import com.stylesync.view.components.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;

/**
 * Dialog for adding new clothing items with image upload
 * Uses HashSet for categories (Collections Framework)
 */
public class AddItemDialog extends JDialog {
    private User currentUser;
    private WardrobeView parentView;
    
    private ModernTextField nameField;
    private JComboBox<String> typeCombo;
    private JComboBox<String> colorCombo;
    private JTextArea notesArea;
    
    // Collections Framework - HashSet for multi-select categories
    private HashSet<String> selectedSeasons;
    private HashSet<String> selectedOccasions;
    private HashSet<String> selectedStyles;
    
    private JPanel imagePreviewPanel;
    private JLabel imagePreviewLabel;
    private File selectedImageFile;
    private BufferedImage capturedImage;
    private String imageUrl;
    private String imageSource;

    public AddItemDialog(Frame parent, User user, WardrobeView wardrobeView) {
        super(parent, "Add Clothing", true);
        this.currentUser = user;
        this.parentView = wardrobeView;
        this.selectedSeasons = new HashSet<>();
        this.selectedOccasions = new HashSet<>();
        this.selectedStyles = new HashSet<>();
        initializeUI();
    }

    private void initializeUI() {
        setSize(700, 750);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Title
        JLabel titleLabel = new JLabel("Add Clothing");
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

        // Image upload section
        panel.add(createImageUploadSection());
        panel.add(Box.createVerticalStrut(20));

        // Name field
        panel.add(createLabel("Name *"));
        nameField = new ModernTextField("e.g., Blue Denim Jacket");
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(15));

        // Type combo
        panel.add(createLabel("Category *"));
        String[] types = {"Top", "Bottom", "Dress", "Outerwear", "Shoes", "Accessories"};
        typeCombo = createComboBox(types);
        panel.add(typeCombo);
        panel.add(Box.createVerticalStrut(15));

        // Color combo
        panel.add(createLabel("Color *"));
        String[] colors = {"Black", "White", "Red", "Blue", "Green", "Yellow", "Pink", "Purple", "Gray", "Brown", "Orange"};
        colorCombo = createComboBox(colors);
        panel.add(colorCombo);
        panel.add(Box.createVerticalStrut(15));

        // Season checkboxes - using HashSet
        panel.add(createLabel("Season"));
        panel.add(createCheckboxPanel(new String[]{"Spring", "Summer", "Fall", "Winter"}, selectedSeasons));
        panel.add(Box.createVerticalStrut(15));

        // Occasion checkboxes - using HashSet
        panel.add(createLabel("Occasion"));
        panel.add(createCheckboxPanel(new String[]{"Casual", "Formal", "Party", "Work", "Sport"}, selectedOccasions));
        panel.add(Box.createVerticalStrut(15));

        // Style checkboxes - using HashSet
        panel.add(createLabel("Style"));
        panel.add(createCheckboxPanel(new String[]{"Elegant", "Sporty", "Casual", "Vintage", "Modern"}, selectedStyles));
        panel.add(Box.createVerticalStrut(15));

        // Notes
        panel.add(createLabel("Notes"));
        notesArea = new JTextArea(3, 20);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.add(notesScroll);

        return panel;
    }

    private JPanel createImageUploadSection() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        // Upload buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        JLabel uploadLabel = createLabel("Upload Image *");
        buttonPanel.add(uploadLabel);
        buttonPanel.add(Box.createVerticalStrut(10));

        JButton fileButton = new JButton("📁 From File");
        styleUploadButton(fileButton);
        fileButton.addActionListener(e -> selectImageFromFile());
        buttonPanel.add(fileButton);
        buttonPanel.add(Box.createVerticalStrut(8));

        JButton cameraButton = new JButton("📷 From Camera");
        styleUploadButton(cameraButton);
        cameraButton.addActionListener(e -> captureFromCamera());
        buttonPanel.add(cameraButton);
        buttonPanel.add(Box.createVerticalStrut(8));

        JButton urlButton = new JButton("🔗 From URL");
        styleUploadButton(urlButton);
        urlButton.addActionListener(e -> loadFromURL());
        buttonPanel.add(urlButton);

        // Preview panel
        imagePreviewPanel = new JPanel(new BorderLayout());
        imagePreviewPanel.setBackground(StyleSyncColors.BACKGROUND);
        imagePreviewPanel.setBorder(BorderFactory.createLineBorder(StyleSyncColors.BORDER, 2));
        imagePreviewPanel.setPreferredSize(new Dimension(300, 250));

        imagePreviewLabel = new JLabel("No image selected", SwingConstants.CENTER);
        imagePreviewLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
        imagePreviewPanel.add(imagePreviewLabel, BorderLayout.CENTER);

        panel.add(buttonPanel, BorderLayout.WEST);
        panel.add(imagePreviewPanel, BorderLayout.CENTER);

        return panel;
    }

    private void styleUploadButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleSyncColors.BORDER, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void selectImageFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            imageSource = "FILE";
            capturedImage = null;
            imageUrl = null;
            displayImagePreview(selectedImageFile);
        }
    }

    private void captureFromCamera() {
        try {
            Webcam webcam = Webcam.getDefault();
            if (webcam == null) {
                JOptionPane.showMessageDialog(this, "No webcam detected!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simple capture dialog
            JDialog captureDialog = new JDialog(this, "Capture Photo", true);
            captureDialog.setSize(700, 600);
            captureDialog.setLocationRelativeTo(this);

            JPanel capturePanel = new JPanel(new BorderLayout());
            JLabel cameraLabel = new JLabel();
            cameraLabel.setHorizontalAlignment(SwingConstants.CENTER);

            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();

            Timer timer = new Timer(50, e -> {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    ImageIcon icon = new ImageIcon(image.getScaledInstance(640, 480, Image.SCALE_FAST));
                    cameraLabel.setIcon(icon);
                }
            });
            timer.start();

            JButton captureBtn = new ModernButton("Capture");
            captureBtn.addActionListener(e -> {
                capturedImage = webcam.getImage();
                imageSource = "CAMERA";
                selectedImageFile = null;
                imageUrl = null;
                timer.stop();
                webcam.close();
                captureDialog.dispose();
                displayCapturedImage(capturedImage);
            });

            capturePanel.add(cameraLabel, BorderLayout.CENTER);
            capturePanel.add(captureBtn, BorderLayout.SOUTH);
            captureDialog.add(capturePanel);
            captureDialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Camera error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadFromURL() {
        String url = JOptionPane.showInputDialog(this, "Enter image URL:", "Load from URL", JOptionPane.PLAIN_MESSAGE);
        if (url != null && !url.trim().isEmpty()) {
            imageUrl = url.trim();
            imageSource = "URL";
            selectedImageFile = null;
            capturedImage = null;
            
            imagePreviewLabel.setText("Image URL set: " + url);
            imagePreviewLabel.setIcon(null);
        }
    }

    private void displayImagePreview(File file) {
        try {
            BufferedImage img = ImageIO.read(file);
            displayImageIcon(img);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load image", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayCapturedImage(BufferedImage img) {
        displayImageIcon(img);
    }

    private void displayImageIcon(BufferedImage img) {
        Image scaled = img.getScaledInstance(280, 230, Image.SCALE_SMOOTH);
        imagePreviewLabel.setIcon(new ImageIcon(scaled));
        imagePreviewLabel.setText("");
    }

    private JPanel createCheckboxPanel(String[] options, HashSet<String> selectedSet) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        for (String option : options) {
            JCheckBox checkbox = new JCheckBox(option);
            checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            checkbox.setBackground(Color.WHITE);
            checkbox.setFocusPainted(false);
            checkbox.addActionListener(e -> {
                if (checkbox.isSelected()) {
                    selectedSet.add(option);
                } else {
                    selectedSet.remove(option);
                }
            });
            panel.add(checkbox);
        }

        return panel;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        combo.setBackground(Color.WHITE);
        return combo;
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

        ModernButton saveButton = new ModernButton("Add Item");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.addActionListener(e -> saveItem());

        panel.add(cancelButton);
        panel.add(saveButton);

        return panel;
    }

    private void saveItem() {
        // Validation
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter item name", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedImageFile == null && capturedImage == null && imageUrl == null) {
            JOptionPane.showMessageDialog(this, "Please upload an image", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ClothingItem item = new ClothingItem();
            item.setName(nameField.getText().trim());
            item.setType((String) typeCombo.getSelectedItem());
            item.setColor((String) colorCombo.getSelectedItem());
            item.setSeasons(new HashSet<>(selectedSeasons));
            item.setOccasions(new HashSet<>(selectedOccasions));
            item.setStyles(new HashSet<>(selectedStyles));
            item.setNotes(notesArea.getText().trim());

            // Save based on source
            if ("FILE".equals(imageSource)) {
                parentView.getController().addItemFromFile(selectedImageFile, item);
            } else if ("CAMERA".equals(imageSource)) {
                parentView.getController().addItemFromCamera(capturedImage, item);
            } else if ("URL".equals(imageSource)) {
                parentView.getController().addItemFromURL(imageUrl, item);
            }

            JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parentView.refreshItems();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
