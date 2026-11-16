package com.stylesync.service;

import com.stylesync.dao.ClothingItemDAO;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.model.WardrobeStats;
import com.stylesync.util.ImageUtil;
import org.bson.types.ObjectId;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for wardrobe management operations
 */
public class WardrobeService {
    private final ClothingItemDAO clothingItemDAO;

    public WardrobeService() {
        this.clothingItemDAO = new ClothingItemDAO();
    }

    public ClothingItem addClothingItemFromFile(User user, File imageFile, ClothingItem item) throws Exception {
        String imagePath = ImageUtil.saveImage(imageFile);
        item.setImagePath(imagePath);
        item.setImageSource("FILE");
        item.setUserId(user.getId());
        return clothingItemDAO.save(item);
    }

    public ClothingItem addClothingItemFromCamera(User user, BufferedImage image, ClothingItem item) throws Exception {
        String imagePath = ImageUtil.saveImageFromCamera(image);
        item.setImagePath(imagePath);
        item.setImageSource("CAMERA");
        item.setUserId(user.getId());
        return clothingItemDAO.save(item);
    }

    public ClothingItem addClothingItemFromURL(User user, String imageUrl, ClothingItem item) throws Exception {
        String imagePath = ImageUtil.saveImageFromURL(imageUrl);
        item.setImagePath(imagePath);
        item.setImageSource("URL");
        item.setUserId(user.getId());
        return clothingItemDAO.save(item);
    }

    public ClothingItem updateClothingItem(ClothingItem item) {
        clothingItemDAO.update(item);
        return item;
    }

    public void deleteClothingItem(ObjectId itemId) {
        ClothingItem item = clothingItemDAO.findById(itemId);
        if (item != null && item.getImagePath() != null) {
            ImageUtil.deleteImage(item.getImagePath());
        }
        clothingItemDAO.delete(itemId);
    }

    public ClothingItem getClothingItem(ObjectId itemId) {
        return clothingItemDAO.findById(itemId);
    }

    public List<ClothingItem> getAllClothingItems(User user) {
        return clothingItemDAO.findByUserId(user.getId());
    }

    public List<ClothingItem> getFavoriteItems(User user) {
        return clothingItemDAO.findFavoritesByUserId(user.getId());
    }

    public List<ClothingItem> searchClothingItems(User user, String searchText, String type,
                                                   String color, String season, String occasion) {
        return clothingItemDAO.searchItems(user.getId(), searchText, type, color, season, occasion);
    }

    public List<ClothingItem> sortItems(List<ClothingItem> items, String sortBy) {
        Comparator<ClothingItem> comparator;

        switch (sortBy) {
            case "Name (A-Z)":
                comparator = Comparator.comparing(ClothingItem::getName);
                break;
            case "Name (Z-A)":
                comparator = Comparator.comparing(ClothingItem::getName).reversed();
                break;
            case "Most Worn":
                comparator = Comparator.comparing(ClothingItem::getWearCount).reversed();
                break;
            case "Least Worn":
                comparator = Comparator.comparing(ClothingItem::getWearCount);
                break;
            case "Recently Added":
                comparator = Comparator.comparing(ClothingItem::getAddedDate).reversed();
                break;
            case "Oldest First":
                comparator = Comparator.comparing(ClothingItem::getAddedDate);
                break;
            default:
                return items;
        }

        return items.stream().sorted(comparator).collect(Collectors.toList());
    }

    public void toggleFavorite(ClothingItem item) {
        item.setFavorite(!item.isFavorite());
        clothingItemDAO.update(item);
    }

    public void markAsWorn(ClothingItem item) {
        item.incrementWearCount();
        clothingItemDAO.update(item);
    }

    public WardrobeStats getWardrobeStats(User user) {
        List<ClothingItem> items = getAllClothingItems(user);
        WardrobeStats stats = new WardrobeStats();

        stats.setTotalItems(items.size());

        // Count by type
        Map<String, Integer> byType = new HashMap<>();
        for (ClothingItem item : items) {
            byType.merge(item.getType(), 1, Integer::sum);
        }
        stats.setItemsByType(byType);

        // Count by color
        Map<String, Integer> byColor = new HashMap<>();
        for (ClothingItem item : items) {
            byColor.merge(item.getColor(), 1, Integer::sum);
        }
        stats.setItemsByColor(byColor);

        // Count by season
        Map<String, Integer> bySeason = new HashMap<>();
        for (ClothingItem item : items) {
            for (String season : item.getSeasons()) {
                bySeason.merge(season, 1, Integer::sum);
            }
        }
        stats.setItemsBySeason(bySeason);

        // Count by occasion
        Map<String, Integer> byOccasion = new HashMap<>();
        for (ClothingItem item : items) {
            for (String occasion : item.getOccasions()) {
                byOccasion.merge(occasion, 1, Integer::sum);
            }
        }
        stats.setItemsByOccasion(byOccasion);

        // Count favorites
        long favoriteCount = items.stream().filter(ClothingItem::isFavorite).count();
        stats.setFavoriteItems((int) favoriteCount);

        // Most and least worn
        if (!items.isEmpty()) {
            stats.setMostWornItem(items.stream()
                    .max(Comparator.comparing(ClothingItem::getWearCount))
                    .orElse(null));
            stats.setLeastWornItem(items.stream()
                    .min(Comparator.comparing(ClothingItem::getWearCount))
                    .orElse(null));
        }

        // Dominant color and type
        stats.setDominantColor(findDominant(byColor));
        stats.setDominantType(findDominant(byType));

        return stats;
    }

    private String findDominant(Map<String, Integer> map) {
        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }
}
