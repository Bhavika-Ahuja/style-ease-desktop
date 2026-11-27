package com.stylesync.view;

import com.stylesync.model.ClothingItem;
import com.stylesync.model.Outfit;
import com.stylesync.model.User;
import com.stylesync.service.OutfitService;
import com.stylesync.util.ImageUtil;
import com.stylesync.util.WeatherService;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Recommendation view with smart outfit suggestions - using Collections Framework
 */
public class RecommendationView extends JPanel {
    private User currentUser;
    private JFrame parentView; // Changed to JFrame to work with both DashboardView and ImprovedDashboardView
    private OutfitService outfitService;
    private WeatherService weatherService;
    
    // Collections Framework - ArrayList for recommendations
    private ArrayList<Outfit> recommendations;
    
    private JPanel recommendationsPanel;
    private ModernTextField cityField;
    private JComboBox<String> occasionCombo;
    private JLabel weatherLabel;

    public RecommendationView(User user, JFrame parent) {
        this.currentUser = user;
        this.parentView = parent;
        this.outfitService = new OutfitService();
        this.weatherService = new WeatherService();
        this.recommendations = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleSyncColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top panel with weather and filters
        add(createTopPanel(), BorderLayout.NORTH);

        // Center panel with recommendations
        JScrollPane scrollPane = new JScrollPane(createRecommendationsPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(StyleSyncColors.BACKGROUND);

        // Title
        JLabel titleLabel = new JLabel("Smart Recommendations");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Weather panel with gradient
        RoundedPanel weatherPanel = new RoundedPanel(15) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = StyleSyncColors.createHorizontalGradient(getWidth());
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        weatherPanel.setLayout(new BorderLayout());
        weatherPanel.setPreferredSize(new Dimension(0, 120));
        weatherPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel weatherContent = new JPanel(new BorderLayout());
        weatherContent.setOpaque(false);

        weatherLabel = new JLabel("Weather in Mumbai: partly cloudy, 22.0°C");
        weatherLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        weatherLabel.setForeground(Color.WHITE);

        JLabel weatherSubLabel = new JLabel("Get personalized outfit recommendations");
        weatherSubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        weatherSubLabel.setForeground(new Color(255, 255, 255, 230));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(weatherLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(weatherSubLabel);

        weatherContent.add(textPanel, BorderLayout.WEST);
        weatherPanel.add(weatherContent, BorderLayout.CENTER);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(StyleSyncColors.BACKGROUND);
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        cityField = new ModernTextField("Enter city (e.g., Mumbai)");
        cityField.setPreferredSize(new Dimension(200, 40));

        String[] occasions = {"Casual", "Formal", "Party", "Work", "Sport"};
        occasionCombo = new JComboBox<>(occasions);
        occasionCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        occasionCombo.setPreferredSize(new Dimension(140, 40));
        occasionCombo.setBackground(Color.WHITE);

        ModernButton getRecommendationsBtn = new ModernButton("Get Recommendations", false);
        getRecommendationsBtn.setBackground(StyleSyncColors.SUCCESS);
        getRecommendationsBtn.setPreferredSize(new Dimension(180, 40));
        getRecommendationsBtn.addActionListener(e -> generateRecommendations());

        filterPanel.add(new JLabel("City:"));
        filterPanel.add(cityField);
        filterPanel.add(new JLabel("Occasion:"));
        filterPanel.add(occasionCombo);
        filterPanel.add(getRecommendationsBtn);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(weatherPanel);
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(filterPanel);
        topPanel.add(Box.createVerticalStrut(20));

        return topPanel;
    }

    private JPanel createRecommendationsPanel() {
        recommendationsPanel = new JPanel();
        recommendationsPanel.setLayout(new BoxLayout(recommendationsPanel, BoxLayout.Y_AXIS));
        recommendationsPanel.setBackground(StyleSyncColors.BACKGROUND);
        recommendationsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("Enter your city and occasion to get personalized recommendations");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        infoLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        recommendationsPanel.add(Box.createVerticalStrut(50));
        recommendationsPanel.add(infoLabel);

        return recommendationsPanel;
    }

    private void generateRecommendations() {
        String city = cityField.getText().trim();
        String occasion = (String) occasionCombo.getSelectedItem();

        if (city.isEmpty()) {
            city = "Mumbai"; // Default
        }

        // Get weather info
        WeatherService.WeatherInfo weather = weatherService.getWeather(city);
        weatherLabel.setText("Weather in " + city + ": " + weather.toString());

        // Get recommendations - using ArrayList (Collections Framework)
        recommendations.clear();
        recommendations.addAll(outfitService.recommendOutfits(currentUser, weather.getDescription(), occasion));

        displayRecommendations();
    }

    private void displayRecommendations() {
        recommendationsPanel.removeAll();

        if (recommendations.isEmpty()) {
            JLabel emptyLabel = new JLabel("No recommendations available. Add more items to your wardrobe!");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            recommendationsPanel.add(Box.createVerticalStrut(50));
            recommendationsPanel.add(emptyLabel);
        } else {
            // Display recommendations using ArrayList iteration
            for (int i = 0; i < recommendations.size(); i++) {
                Outfit outfit = recommendations.get(i);
                recommendationsPanel.add(createRecommendationCard(outfit, i + 1));
                recommendationsPanel.add(Box.createVerticalStrut(15));
            }
        }

        recommendationsPanel.revalidate();
        recommendationsPanel.repaint();
    }

    private RoundedPanel createRecommendationCard(Outfit outfit, int number) {
        RoundedPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout(20, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Number badge
        JLabel numberLabel = new JLabel(String.valueOf(number));
        numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        numberLabel.setForeground(StyleSyncColors.PRIMARY);
        numberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numberLabel.setPreferredSize(new Dimension(60, 60));

        // Get outfit items using service - ArrayList
        java.util.List<ClothingItem> items = outfitService.getOutfitItems(outfit);
        ArrayList<ClothingItem> itemsList = new ArrayList<>(items);

        // Items panel - show thumbnails
        JPanel itemsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        itemsPanel.setBackground(Color.WHITE);

        // Using ArrayList - limit to first 5 items
        for (int i = 0; i < Math.min(5, itemsList.size()); i++) {
            ClothingItem item = itemsList.get(i);
            try {
                ImageIcon icon = ImageUtil.getScaledImageIcon(item.getImagePath(), 100, 120);
                JLabel imageLabel = new JLabel(icon);
                itemsPanel.add(imageLabel);
            } catch (Exception e) {
                JLabel placeholder = new JLabel("No Image");
                placeholder.setPreferredSize(new Dimension(100, 120));
                placeholder.setHorizontalAlignment(SwingConstants.CENTER);
                placeholder.setForeground(StyleSyncColors.TEXT_SECONDARY);
                itemsPanel.add(placeholder);
            }
        }

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(outfit.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel detailsLabel = new JLabel(itemsList.size() + " items • " + outfit.getOccasion() + " • " + outfit.getSeason());
        detailsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        ModernButton saveButton = new ModernButton("Save Outfit", false);
        saveButton.setBackground(StyleSyncColors.PRIMARY);
        saveButton.setPreferredSize(new Dimension(120, 35));
        saveButton.addActionListener(e -> saveRecommendation(outfit, itemsList));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(detailsLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(saveButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(itemsPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.EAST);

        card.add(numberLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private void saveRecommendation(Outfit outfit, ArrayList<ClothingItem> items) {
        try {
            outfitService.createOutfit(
                    currentUser,
                    outfit.getName(),
                    items,
                    outfit.getOccasion(),
                    outfit.getSeason(),
                    "Recommended outfit"
            );
            JOptionPane.showMessageDialog(this, "Outfit saved to your collection!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving outfit: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
