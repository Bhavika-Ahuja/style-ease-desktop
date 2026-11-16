package com.stylesync.controller;

import com.stylesync.model.ClothingItem;
import com.stylesync.model.Outfit;
import com.stylesync.model.User;
import com.stylesync.service.OutfitService;
import com.stylesync.view.OutfitView;

import java.util.*;

/**
 * Controller for outfit operations - using Collections Framework
 */
public class OutfitController {
    private final OutfitView view;
    private final User currentUser;
    private final OutfitService outfitService;

    public OutfitController(OutfitView view, User user) {
        this.view = view;
        this.currentUser = user;
        this.outfitService = new OutfitService();
    }

    public ArrayList<Outfit> getAllOutfits() {
        List<Outfit> outfits = outfitService.getAllOutfits(currentUser);
        return new ArrayList<>(outfits);
    }

    public ArrayList<ClothingItem> getOutfitItems(Outfit outfit) {
        List<ClothingItem> items = outfitService.getOutfitItems(outfit);
        return new ArrayList<>(items);
    }

    public Outfit createOutfit(String name, ArrayList<ClothingItem> items, 
                              String occasion, String season, String notes) {
        return outfitService.createOutfit(currentUser, name, items, occasion, season, notes);
    }

    public void deleteOutfit(Outfit outfit) {
        outfitService.deleteOutfit(outfit.getId());
    }

    public void toggleFavorite(Outfit outfit) {
        outfitService.toggleFavorite(outfit);
    }

    public void markAsWorn(Outfit outfit) {
        outfitService.markAsWorn(outfit);
    }

    public ArrayList<Outfit> getFavoriteOutfits() {
        List<Outfit> favorites = outfitService.getFavoriteOutfits(currentUser);
        return new ArrayList<>(favorites);
    }
}
