package com.stylesync.model;

import java.util.HashMap;
import java.util.Map;

/**
 * WardrobeStats model for analytics
 */
public class WardrobeStats {
    private int totalItems;
    private int totalOutfits;
    private Map<String, Integer> itemsByType;
    private Map<String, Integer> itemsByColor;
    private Map<String, Integer> itemsBySeason;
    private Map<String, Integer> itemsByOccasion;
    private int favoriteItems;
    private ClothingItem mostWornItem;
    private ClothingItem leastWornItem;
    private String dominantColor;
    private String dominantType;

    public WardrobeStats() {
        this.itemsByType = new HashMap<>();
        this.itemsByColor = new HashMap<>();
        this.itemsBySeason = new HashMap<>();
        this.itemsByOccasion = new HashMap<>();
    }

    // Getters and Setters
    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalOutfits() {
        return totalOutfits;
    }

    public void setTotalOutfits(int totalOutfits) {
        this.totalOutfits = totalOutfits;
    }

    public Map<String, Integer> getItemsByType() {
        return itemsByType;
    }

    public void setItemsByType(Map<String, Integer> itemsByType) {
        this.itemsByType = itemsByType;
    }

    public Map<String, Integer> getItemsByColor() {
        return itemsByColor;
    }

    public void setItemsByColor(Map<String, Integer> itemsByColor) {
        this.itemsByColor = itemsByColor;
    }

    public Map<String, Integer> getItemsBySeason() {
        return itemsBySeason;
    }

    public void setItemsBySeason(Map<String, Integer> itemsBySeason) {
        this.itemsBySeason = itemsBySeason;
    }

    public Map<String, Integer> getItemsByOccasion() {
        return itemsByOccasion;
    }

    public void setItemsByOccasion(Map<String, Integer> itemsByOccasion) {
        this.itemsByOccasion = itemsByOccasion;
    }

    public int getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(int favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    public ClothingItem getMostWornItem() {
        return mostWornItem;
    }

    public void setMostWornItem(ClothingItem mostWornItem) {
        this.mostWornItem = mostWornItem;
    }

    public ClothingItem getLeastWornItem() {
        return leastWornItem;
    }

    public void setLeastWornItem(ClothingItem leastWornItem) {
        this.leastWornItem = leastWornItem;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public String getDominantType() {
        return dominantType;
    }

    public void setDominantType(String dominantType) {
        this.dominantType = dominantType;
    }
}
