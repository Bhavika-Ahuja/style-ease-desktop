package com.stylesync.service;

import com.stylesync.dao.ClothingItemDAO;
import com.stylesync.dao.OutfitDAO;
import com.stylesync.model.ClothingItem;
import com.stylesync.model.Outfit;
import com.stylesync.model.User;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for outfit management operations
 */
public class OutfitService {
    private final OutfitDAO outfitDAO;
    private final ClothingItemDAO clothingItemDAO;

    public OutfitService() {
        this.outfitDAO = new OutfitDAO();
        this.clothingItemDAO = new ClothingItemDAO();
    }

    public Outfit createOutfit(User user, String name, List<ClothingItem> items,
                               String occasion, String season, String notes) {
        Outfit outfit = new Outfit();
        outfit.setUserId(user.getId());
        outfit.setName(name);
        outfit.setClothingItemIds(items.stream()
                .map(ClothingItem::getId)
                .collect(Collectors.toList()));
        outfit.setOccasion(occasion);
        outfit.setSeason(season);
        outfit.setNotes(notes);
        return outfitDAO.save(outfit);
    }

    public Outfit updateOutfit(Outfit outfit) {
        outfitDAO.update(outfit);
        return outfit;
    }

    public void deleteOutfit(ObjectId outfitId) {
        outfitDAO.delete(outfitId);
    }

    public Outfit getOutfit(ObjectId outfitId) {
        return outfitDAO.findById(outfitId);
    }

    public List<Outfit> getAllOutfits(User user) {
        return outfitDAO.findByUserId(user.getId());
    }

    public List<Outfit> getFavoriteOutfits(User user) {
        return outfitDAO.findFavoritesByUserId(user.getId());
    }

    public List<ClothingItem> getOutfitItems(Outfit outfit) {
        List<ClothingItem> items = new ArrayList<>();
        for (ObjectId itemId : outfit.getClothingItemIds()) {
            ClothingItem item = clothingItemDAO.findById(itemId);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public void toggleFavorite(Outfit outfit) {
        outfit.setFavorite(!outfit.isFavorite());
        outfitDAO.update(outfit);
    }

    public void markAsWorn(Outfit outfit) {
        outfit.incrementWearCount();
        outfitDAO.update(outfit);

        // Also increment wear count for all items
        for (ObjectId itemId : outfit.getClothingItemIds()) {
            ClothingItem item = clothingItemDAO.findById(itemId);
            if (item != null) {
                item.incrementWearCount();
                clothingItemDAO.update(item);
            }
        }
    }

    public List<Outfit> recommendOutfits(User user, String weather, String occasion) {
        List<ClothingItem> allItems = clothingItemDAO.findByUserId(user.getId());
        List<Outfit> recommendations = new ArrayList<>();

        // Determine season based on weather
        String season = getSeasonFromWeather(weather);

        // Filter items by season and occasion
        List<ClothingItem> suitableItems = allItems.stream()
                .filter(item -> item.getSeasons().contains(season) ||
                        item.getOccasions().contains(occasion))
                .collect(Collectors.toList());

        // Group by type
        Map<String, List<ClothingItem>> itemsByType = suitableItems.stream()
                .collect(Collectors.groupingBy(ClothingItem::getType));

        // Generate outfit combinations (simplified algorithm)
        List<ClothingItem> tops = itemsByType.getOrDefault("Top", new ArrayList<>());
        List<ClothingItem> bottoms = itemsByType.getOrDefault("Bottom", new ArrayList<>());
        List<ClothingItem> shoes = itemsByType.getOrDefault("Shoes", new ArrayList<>());

        int count = 0;
        for (ClothingItem top : tops) {
            for (ClothingItem bottom : bottoms) {
                if (count >= 5) break; // Limit to 5 recommendations
                
                List<ClothingItem> outfitItems = new ArrayList<>();
                outfitItems.add(top);
                outfitItems.add(bottom);
                
                if (!shoes.isEmpty()) {
                    outfitItems.add(shoes.get(0));
                }

                Outfit outfit = new Outfit();
                outfit.setName("Recommended Outfit " + (count + 1));
                outfit.setOccasion(occasion);
                outfit.setSeason(season);
                outfit.setClothingItemIds(outfitItems.stream()
                        .map(ClothingItem::getId)
                        .collect(Collectors.toList()));
                recommendations.add(outfit);
                count++;
            }
        }

        return recommendations;
    }

    private String getSeasonFromWeather(String weather) {
        if (weather == null) return "Spring";
        
        weather = weather.toLowerCase();
        if (weather.contains("hot") || weather.contains("sunny") || weather.contains("warm")) {
            return "Summer";
        } else if (weather.contains("cold") || weather.contains("snow") || weather.contains("freezing")) {
            return "Winter";
        } else if (weather.contains("cool") || weather.contains("rainy")) {
            return "Fall";
        }
        return "Spring";
    }
}
