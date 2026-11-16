package com.stylesync.view;

import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.model.WardrobeStats;
import com.stylesync.service.WardrobeService;
import com.stylesync.view.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Analytics view with wardrobe statistics - using Collections Framework extensively
 * Demonstrates: HashMap, TreeSet, PriorityQueue, Comparator
 */
public class AnalyticsView extends JPanel {
    private User currentUser;
    private DashboardView parentView;
    private WardrobeService wardrobeService;
    
    // Collections Framework - HashMap for category counts
    private HashMap<String, Integer> typeDistribution;
    private HashMap<String, Integer> colorDistribution;
    
    // Collections Framework - TreeSet for sorted items (by name)
    private TreeSet<ClothingItem> sortedItemsByName;
    
    // Collections Framework - PriorityQueue for most/least worn items
    private PriorityQueue<ClothingItem> mostWornQueue;
    private PriorityQueue<ClothingItem> leastWornQueue;
    
    private JPanel statsPanel;
    private WardrobeStats stats;

    public AnalyticsView(User user, DashboardView parent) {
        this.currentUser = user;
        this.parentView = parent;
        this.wardrobeService = new WardrobeService();
        
        // Initialize Collections Framework data structures
        this.typeDistribution = new HashMap<>();
        this.colorDistribution = new HashMap<>();
        
        // TreeSet with natural ordering (by name)
        this.sortedItemsByName = new TreeSet<>(Comparator.comparing(ClothingItem::getName));
        
        // PriorityQueue with custom Comparators
        this.mostWornQueue = new PriorityQueue<>(
                Comparator.comparing(ClothingItem::getWearCount).reversed());
        this.leastWornQueue = new PriorityQueue<>(
                Comparator.comparing(ClothingItem::getWearCount));
        
        initializeUI();
        refreshStats();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(StyleSyncColors.BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(StyleSyncColors.BACKGROUND);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Wardrobe Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        add(titlePanel, BorderLayout.NORTH);

        // Stats content
        JScrollPane scrollPane = new JScrollPane(createStatsPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(StyleSyncColors.BACKGROUND);
        return statsPanel;
    }

    public void refreshStats() {
        stats = wardrobeService.getWardrobeStats(currentUser);
        
        // Populate Collections Framework structures
        populateCollections();
        
        // Display stats
        displayStats();
    }

    /**
     * Populate Collections Framework data structures
     * Demonstrates HashMap, TreeSet, PriorityQueue usage
     */
    private void populateCollections() {
        List<ClothingItem> allItems = wardrobeService.getAllClothingItems(currentUser);
        
        // Clear existing data
        typeDistribution.clear();
        colorDistribution.clear();
        sortedItemsByName.clear();
        mostWornQueue.clear();
        leastWornQueue.clear();
        
        // Populate HashMap for type distribution
        for (ClothingItem item : allItems) {
            typeDistribution.put(item.getType(), 
                    typeDistribution.getOrDefault(item.getType(), 0) + 1);
        }
        
        // Populate HashMap for color distribution
        for (ClothingItem item : allItems) {
            colorDistribution.put(item.getColor(), 
                    colorDistribution.getOrDefault(item.getColor(), 0) + 1);
        }
        
        // Populate TreeSet (automatically sorted by name)
        sortedItemsByName.addAll(allItems);
        
        // Populate PriorityQueues for wear statistics
        mostWornQueue.addAll(allItems);
        leastWornQueue.addAll(allItems);
    }

    private void displayStats() {
        statsPanel.removeAll();

        // Summary cards
        statsPanel.add(createSummarySection());
        statsPanel.add(Box.createVerticalStrut(20));

        // Distribution charts
        statsPanel.add(createDistributionSection());
        statsPanel.add(Box.createVerticalStrut(20));

        // Wear statistics using PriorityQueue
        statsPanel.add(createWearStatisticsSection());
        statsPanel.add(Box.createVerticalStrut(20));

        // Sorted items using TreeSet
        statsPanel.add(createSortedItemsSection());

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createSummarySection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleSyncColors.BACKGROUND);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel sectionTitle = new JLabel("Overview");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(StyleSyncColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsPanel.setBackground(StyleSyncColors.BACKGROUND);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        cardsPanel.add(createStatCard("Total Items", String.valueOf(stats.getTotalItems()), 
                "👔", StyleSyncColors.PRIMARY));
        cardsPanel.add(createStatCard("Total Outfits", String.valueOf(stats.getTotalOutfits()), 
                "✨", StyleSyncColors.SECONDARY));
        cardsPanel.add(createStatCard("Favorites", String.valueOf(stats.getFavoriteItems()), 
                "❤️", StyleSyncColors.ERROR));
        cardsPanel.add(createStatCard("Categories", String.valueOf(typeDistribution.size()), 
                "📂", StyleSyncColors.INFO));

        section.add(cardsPanel);
        return section;
    }

    private RoundedPanel createStatCard(String title, String value, String emoji, Color color) {
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);

        content.add(emojiLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(valueLabel);
        content.add(Box.createVerticalStrut(3));
        content.add(titleLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /**
     * Distribution section using HashMap
     */
    private JPanel createDistributionSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleSyncColors.BACKGROUND);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel sectionTitle = new JLabel("Distribution Analysis (HashMap)");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(StyleSyncColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(StyleSyncColors.BACKGROUND);
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        // Type distribution using HashMap
        chartsPanel.add(createDistributionChart("By Category", typeDistribution, 
                new Color[]{StyleSyncColors.PRIMARY, StyleSyncColors.SECONDARY, 
                        StyleSyncColors.SUCCESS, StyleSyncColors.WARNING, 
                        StyleSyncColors.INFO, StyleSyncColors.ERROR}));

        // Color distribution using HashMap
        chartsPanel.add(createDistributionChart("By Color", colorDistribution, 
                new Color[]{Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, 
                        StyleSyncColors.PRIMARY, Color.ORANGE}));

        section.add(chartsPanel);
        return section;
    }

    /**
     * Create distribution chart from HashMap
     * Demonstrates HashMap iteration and sorting
     */
    private RoundedPanel createDistributionChart(String title, HashMap<String, Integer> data, Color[] colors) {
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        card.add(titleLabel, BorderLayout.NORTH);

        // Sort HashMap by value (descending) using Collections
        ArrayList<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(data.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        JPanel barsPanel = new JPanel();
        barsPanel.setLayout(new BoxLayout(barsPanel, BoxLayout.Y_AXIS));
        barsPanel.setBackground(Color.WHITE);
        barsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        int maxValue = sortedEntries.isEmpty() ? 1 : sortedEntries.get(0).getValue();
        int colorIndex = 0;

        // Iterate through sorted HashMap entries
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            JPanel barPanel = createBarChart(entry.getKey(), entry.getValue(), 
                    maxValue, colors[colorIndex % colors.length]);
            barsPanel.add(barPanel);
            barsPanel.add(Box.createVerticalStrut(10));
            colorIndex++;
        }

        JScrollPane scrollPane = new JScrollPane(barsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private JPanel createBarChart(String label, int value, int maxValue, Color color) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        nameLabel.setPreferredSize(new Dimension(100, 25));

        JPanel barContainer = new JPanel(new BorderLayout());
        barContainer.setBackground(StyleSyncColors.BACKGROUND);
        barContainer.setPreferredSize(new Dimension(200, 25));

        int barWidth = (int) ((double) value / maxValue * 180);
        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(barWidth, 25));

        JLabel valueLabel = new JLabel(" " + value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        valueLabel.setForeground(Color.WHITE);
        bar.add(valueLabel);

        barContainer.add(bar, BorderLayout.WEST);

        panel.add(nameLabel, BorderLayout.WEST);
        panel.add(barContainer, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Wear statistics using PriorityQueue
     * Demonstrates PriorityQueue with Comparator
     */
    private JPanel createWearStatisticsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleSyncColors.BACKGROUND);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel sectionTitle = new JLabel("Wear Statistics (PriorityQueue)");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(StyleSyncColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));

        JPanel statsCardsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsCardsPanel.setBackground(StyleSyncColors.BACKGROUND);
        statsCardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        // Most worn items from PriorityQueue
        statsCardsPanel.add(createTopItemsCard("Most Worn Items", true));

        // Least worn items from PriorityQueue
        statsCardsPanel.add(createTopItemsCard("Least Worn Items", false));

        section.add(statsCardsPanel);
        return section;
    }

    /**
     * Create top items card from PriorityQueue
     */
    private RoundedPanel createTopItemsCard(String title, boolean mostWorn) {
        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Get items from appropriate PriorityQueue
        PriorityQueue<ClothingItem> queue = mostWorn ? 
                new PriorityQueue<>(mostWornQueue) : new PriorityQueue<>(leastWornQueue);
        
        int count = 0;
        while (!queue.isEmpty() && count < 5) {
            ClothingItem item = queue.poll();
            JPanel itemRow = createItemRow(item, mostWorn);
            itemsPanel.add(itemRow);
            itemsPanel.add(Box.createVerticalStrut(8));
            count++;
        }

        if (count == 0) {
            JLabel emptyLabel = new JLabel("No items yet");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            itemsPanel.add(emptyLabel);
        }

        card.add(itemsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createItemRow(ClothingItem item, boolean showWearCount) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nameLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);

        JLabel countLabel = new JLabel(item.getWearCount() + " times");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        countLabel.setForeground(showWearCount ? StyleSyncColors.SUCCESS : StyleSyncColors.WARNING);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(countLabel, BorderLayout.EAST);

        return row;
    }

    /**
     * Sorted items section using TreeSet
     * Demonstrates TreeSet natural ordering
     */
    private JPanel createSortedItemsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(StyleSyncColors.BACKGROUND);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel sectionTitle = new JLabel("Alphabetically Sorted Items (TreeSet)");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(StyleSyncColors.TEXT_PRIMARY);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(sectionTitle);
        section.add(Box.createVerticalStrut(15));

        RoundedPanel card = new RoundedPanel(12);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);

        // TreeSet automatically maintains sorted order
        int count = 0;
        for (ClothingItem item : sortedItemsByName) {
            if (count >= 10) break; // Show first 10
            
            JLabel itemLabel = new JLabel("• " + item.getName() + " (" + item.getType() + ")");
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            itemLabel.setForeground(StyleSyncColors.TEXT_PRIMARY);
            itemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            itemsPanel.add(itemLabel);
            itemsPanel.add(Box.createVerticalStrut(5));
            count++;
        }

        if (sortedItemsByName.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items in wardrobe");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            emptyLabel.setForeground(StyleSyncColors.TEXT_SECONDARY);
            itemsPanel.add(emptyLabel);
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        card.add(scrollPane, BorderLayout.CENTER);

        section.add(card);
        return section;
    }
}
