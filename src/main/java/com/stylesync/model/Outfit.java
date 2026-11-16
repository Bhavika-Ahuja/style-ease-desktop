package com.stylesync.model;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Outfit model representing a combination of clothing items
 */
public class Outfit {
    private ObjectId id;
    private ObjectId userId;
    private String name;
    private List<ObjectId> clothingItemIds;
    private String occasion;
    private String season;
    private Date createdDate;
    private int wearCount;
    private Date lastWornDate;
    private boolean isFavorite;
    private String notes;

    public Outfit() {
        this.createdDate = new Date();
        this.clothingItemIds = new ArrayList<>();
        this.wearCount = 0;
        this.isFavorite = false;
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectId> getClothingItemIds() {
        return clothingItemIds;
    }

    public void setClothingItemIds(List<ObjectId> clothingItemIds) {
        this.clothingItemIds = clothingItemIds;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getWearCount() {
        return wearCount;
    }

    public void setWearCount(int wearCount) {
        this.wearCount = wearCount;
    }

    public Date getLastWornDate() {
        return lastWornDate;
    }

    public void setLastWornDate(Date lastWornDate) {
        this.lastWornDate = lastWornDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void incrementWearCount() {
        this.wearCount++;
        this.lastWornDate = new Date();
    }

    @Override
    public String toString() {
        return name;
    }
}
