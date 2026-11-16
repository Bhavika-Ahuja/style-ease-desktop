package com.stylesync.controller;

import com.stylesync.model.ClothingItem;
import com.stylesync.model.User;
import com.stylesync.service.WardrobeService;
import com.stylesync.view.WardrobeView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Controller for wardrobe operations - uses Collections Framework
 */
public class WardrobeController {
    private final WardrobeView view;
    private final User currentUser;
    private final WardrobeService wardrobeService;
    
    // Collections Framework - ArrayList for caching
    private ArrayList<ClothingItem> cachedItems;

    public WardrobeController(WardrobeView view, User user) {
        this.view = view;
        this.currentUser = user;
        this.wardrobeService = new WardrobeService();
        this.cachedItems = new ArrayList<>();
    }

    public ArrayList<ClothingItem> getAllItems() {
        List<ClothingItem> items = wardrobeService.getAllClothingItems(currentUser);
        cachedItems = new ArrayList<>(items);
        return cachedItems;
    }

    public ArrayList<ClothingItem> getFavoriteItems() {
        List<ClothingItem> favorites = wardrobeService.getFavoriteItems(currentUser);
        return new ArrayList<>(favorites);
    }

    public ArrayList<ClothingItem> searchAndFilterItems(String searchText, String type, 
                                                        String color, String season, String occasion) {
        List<ClothingItem> results = wardrobeService.searchClothingItems(
                currentUser, searchText, type, color, season, occasion);
        return new ArrayList<>(results);
    }

    /**
     * Sort items using Comparator - Collections Framework
     */
    public ArrayList<ClothingItem> sortItems(ArrayList<ClothingItem> items, String sortBy) {
        ArrayList<ClothingItem> sortedList = new ArrayList<>(items);
        
        Comparator<ClothingItem> comparator = null;
        
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
        }
        
        if (comparator != null) {
            Collections.sort(sortedList, comparator);
        }
        
        return sortedList;
    }

    public ClothingItem addItemFromFile(File file, ClothingItem item) throws Exception {
        return wardrobeService.addClothingItemFromFile(currentUser, file, item);
    }

    public ClothingItem addItemFromCamera(BufferedImage image, ClothingItem item) throws Exception {
        return wardrobeService.addClothingItemFromCamera(currentUser, image, item);
    }

    public ClothingItem addItemFromURL(String url, ClothingItem item) throws Exception {
        return wardrobeService.addClothingItemFromURL(currentUser, url, item);
    }

    public void updateItem(ClothingItem item) {
        wardrobeService.updateClothingItem(item);
    }

    public void deleteItem(ClothingItem item) {
        wardrobeService.deleteClothingItem(item.getId());
    }

    public void toggleFavorite(ClothingItem item) {
        wardrobeService.toggleFavorite(item);
    }

    public void markAsWorn(ClothingItem item) {
        wardrobeService.markAsWorn(item);
    }
}
