package com.stylesync.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.stylesync.model.Outfit;
import com.stylesync.util.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DAO for Outfit entity
 */
public class OutfitDAO {
    private final MongoCollection<Document> collection;

    public OutfitDAO() {
        this.collection = DatabaseConnection.getDatabase().getCollection("outfits");
    }

    public Outfit save(Outfit outfit) {
        Document doc = outfitToDocument(outfit);
        collection.insertOne(doc);
        outfit.setId(doc.getObjectId("_id"));
        return outfit;
    }

    public Outfit findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToOutfit(doc);
    }

    public List<Outfit> findByUserId(ObjectId userId) {
        List<Outfit> outfits = new ArrayList<>();
        collection.find(Filters.eq("userId", userId))
                .sort(Sorts.descending("createdDate"))
                .forEach(doc -> outfits.add(documentToOutfit(doc)));
        return outfits;
    }

    public List<Outfit> findFavoritesByUserId(ObjectId userId) {
        List<Outfit> outfits = new ArrayList<>();
        collection.find(Filters.and(
                        Filters.eq("userId", userId),
                        Filters.eq("isFavorite", true)
                ))
                .forEach(doc -> outfits.add(documentToOutfit(doc)));
        return outfits;
    }

    public void update(Outfit outfit) {
        Document doc = outfitToDocument(outfit);
        doc.remove("_id");
        collection.updateOne(
                Filters.eq("_id", outfit.getId()),
                new Document("$set", doc)
        );
    }

    public void delete(ObjectId id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public long countByUserId(ObjectId userId) {
        return collection.countDocuments(Filters.eq("userId", userId));
    }

    private Document outfitToDocument(Outfit outfit) {
        List<String> itemIds = outfit.getClothingItemIds().stream()
                .map(ObjectId::toHexString)
                .collect(Collectors.toList());

        Document doc = new Document()
                .append("userId", outfit.getUserId())
                .append("name", outfit.getName())
                .append("clothingItemIds", itemIds)
                .append("occasion", outfit.getOccasion())
                .append("season", outfit.getSeason())
                .append("createdDate", outfit.getCreatedDate())
                .append("wearCount", outfit.getWearCount())
                .append("lastWornDate", outfit.getLastWornDate())
                .append("isFavorite", outfit.isFavorite())
                .append("notes", outfit.getNotes());

        if (outfit.getId() != null) {
            doc.append("_id", outfit.getId());
        }
        return doc;
    }

    @SuppressWarnings("unchecked")
    private Outfit documentToOutfit(Document doc) {
        if (doc == null) return null;

        Outfit outfit = new Outfit();
        outfit.setId(doc.getObjectId("_id"));
        outfit.setUserId(doc.getObjectId("userId"));
        outfit.setName(doc.getString("name"));

        List<String> itemIds = (List<String>) doc.get("clothingItemIds");
        if (itemIds != null) {
            List<ObjectId> objectIds = itemIds.stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toList());
            outfit.setClothingItemIds(objectIds);
        }

        outfit.setOccasion(doc.getString("occasion"));
        outfit.setSeason(doc.getString("season"));
        outfit.setCreatedDate(doc.getDate("createdDate"));
        outfit.setWearCount(doc.getInteger("wearCount", 0));
        outfit.setLastWornDate(doc.getDate("lastWornDate"));
        outfit.setFavorite(doc.getBoolean("isFavorite", false));
        outfit.setNotes(doc.getString("notes"));

        return outfit;
    }
}
