package com.stylesync.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ClothingItem model representing a wardrobe item
 */
public class ClothingItem {
    private ObjectId id;
    private ObjectId userId;
    private String name;
    private String type; // Shirt, Pants, Dress, Shoes, Accessories, etc.
    private String color;
    private Set<String> seasons; // Spring, Summer, Fall, Winter
    private Set<String> occasions; // Casual, Formal, Party, Work, Sport
    private Set<String> styles; // Elegant, Sporty, Casual, Vintage, Modern
    private String imagePath;
    private String imageSource; // FILE, CAMERA, URL
    private Date addedDate;
    private int wearCount;
    private Date lastWornDate;
    private boolean isFavorite;
    private String notes;

    public ClothingItem() {
        this.addedDate = new Date();
        this.wearCount = 0;
        this.isFavorite = false;
        this.seasons = new HashSet<>();
        this.occasions = new HashSet<>();
        this.styles = new HashSet<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<String> getSeasons() {
        return seasons;
    }

    public void setSeasons(Set<String> seasons) {
        this.seasons = seasons;
    }

    public Set<String> getOccasions() {
        return occasions;
    }

    public void setOccasions(Set<String> occasions) {
        this.occasions = occasions;
    }

    public Set<String> getStyles() {
        return styles;
    }

    public void setStyles(Set<String> styles) {
        this.styles = styles;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
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
        return name + " (" + type + ")";
    }
}
