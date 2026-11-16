package com.stylesync.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.stylesync.model.ClothingItem;
import com.stylesync.util.DatabaseConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * DAO for ClothingItem entity
 */
public class ClothingItemDAO {
    private final MongoCollection<Document> collection;

    public ClothingItemDAO() {
        this.collection = DatabaseConnection.getDatabase().getCollection("clothing_items");
    }

    public ClothingItem save(ClothingItem item) {
        Document doc = itemToDocument(item);
        collection.insertOne(doc);
        item.setId(doc.getObjectId("_id"));
        return item;
    }

    public ClothingItem findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToItem(doc);
    }

    public List<ClothingItem> findByUserId(ObjectId userId) {
        List<ClothingItem> items = new ArrayList<>();
        collection.find(Filters.eq("userId", userId))
                .sort(Sorts.descending("addedDate"))
                .forEach(doc -> items.add(documentToItem(doc)));
        return items;
    }

    public List<ClothingItem> findFavoritesByUserId(ObjectId userId) {
        List<ClothingItem> items = new ArrayList<>();
        collection.find(Filters.and(
                        Filters.eq("userId", userId),
                        Filters.eq("isFavorite", true)
                ))
                .forEach(doc -> items.add(documentToItem(doc)));
        return items;
    }

    public List<ClothingItem> searchItems(ObjectId userId, String searchText, String type,
                                          String color, String season, String occasion) {
        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.eq("userId", userId));

        if (searchText != null && !searchText.trim().isEmpty()) {
            filters.add(Filters.regex("name", searchText, "i"));
        }
        if (type != null && !type.equals("All")) {
            filters.add(Filters.eq("type", type));
        }
        if (color != null && !color.equals("All")) {
            filters.add(Filters.eq("color", color));
        }
        if (season != null && !season.equals("All")) {
            filters.add(Filters.in("seasons", season));
        }
        if (occasion != null && !occasion.equals("All")) {
            filters.add(Filters.in("occasions", occasion));
        }

        List<ClothingItem> items = new ArrayList<>();
        collection.find(Filters.and(filters))
                .forEach(doc -> items.add(documentToItem(doc)));
        return items;
    }

    public void update(ClothingItem item) {
        Document doc = itemToDocument(item);
        doc.remove("_id");
        collection.updateOne(
                Filters.eq("_id", item.getId()),
                new Document("$set", doc)
        );
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public long countByUserId(ObjectId userId) {
        return collection.countDocuments(Filters.eq("userId", userId));
    }

    private Document itemToDocument(ClothingItem item) {
        Document doc = new Document()
                .append("userId", item.getUserId())
                .append("name", item.getName())
                .append("type", item.getType())
                .append("color", item.getColor())
                .append("seasons", new ArrayList<>(item.getSeasons()))
                .append("occasions", new ArrayList<>(item.getOccasions()))
                .append("styles", new ArrayList<>(item.getStyles()))
                .append("imagePath", item.getImagePath())
                .append("imageSource", item.getImageSource())
                .append("addedDate", item.getAddedDate())
                .append("wearCount", item.getWearCount())
                .append("lastWornDate", item.getLastWornDate())
                .append("isFavorite", item.isFavorite())
                .append("notes", item.getNotes());

        if (item.getId() != null) {
            doc.append("_id", item.getId());
        }
        return doc;
    }

    @SuppressWarnings("unchecked")
    private ClothingItem documentToItem(Document doc) {
        if (doc == null) return null;

        ClothingItem item = new ClothingItem();
        item.setId(doc.getObjectId("_id"));
        item.setUserId(doc.getObjectId("userId"));
        item.setName(doc.getString("name"));
        item.setType(doc.getString("type"));
        item.setColor(doc.getString("color"));
        
        List<String> seasons = (List<String>) doc.get("seasons");
        if (seasons != null) item.setSeasons(new HashSet<>(seasons));
        
        List<String> occasions = (List<String>) doc.get("occasions");
        if (occasions != null) item.setOccasions(new HashSet<>(occasions));
        
        List<String> styles = (List<String>) doc.get("styles");
        if (styles != null) item.setStyles(new HashSet<>(styles));
        
        item.setImagePath(doc.getString("imagePath"));
        item.setImageSource(doc.getString("imageSource"));
        item.setAddedDate(doc.getDate("addedDate"));
        item.setWearCount(doc.getInteger("wearCount", 0));
        item.setLastWornDate(doc.getDate("lastWornDate"));
        item.setFavorite(doc.getBoolean("isFavorite", false));
        item.setNotes(doc.getString("notes"));
        
        return item;
    }
}
