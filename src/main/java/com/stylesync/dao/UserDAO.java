package com.stylesync.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.stylesync.model.User;
import com.stylesync.util.DatabaseConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * DAO for User entity
 */
public class UserDAO {
    private final MongoCollection<Document> collection;

    public UserDAO() {
        this.collection = DatabaseConnection.getDatabase().getCollection("users");
    }

    public User save(User user) {
        Document doc = new Document()
                .append("username", user.getUsername())
                .append("email", user.getEmail())
                .append("passwordHash", user.getPasswordHash())
                .append("fullName", user.getFullName())
                .append("createdAt", user.getCreatedAt())
                .append("lastLogin", user.getLastLogin());

        collection.insertOne(doc);
        user.setId(doc.getObjectId("_id"));
        return user;
    }

    public User findByUsername(String username) {
        Document doc = collection.find(Filters.eq("username", username)).first();
        return documentToUser(doc);
    }

    public User findByEmail(String email) {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return documentToUser(doc);
    }

    public User findById(ObjectId id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return documentToUser(doc);
    }

    public boolean usernameExists(String username) {
        return collection.countDocuments(Filters.eq("username", username)) > 0;
    }

    public boolean emailExists(String email) {
        return collection.countDocuments(Filters.eq("email", email)) > 0;
    }

    public void updateLastLogin(ObjectId userId) {
        collection.updateOne(
                Filters.eq("_id", userId),
                new Document("$set", new Document("lastLogin", new Date()))
        );
    }

    public void updateUser(User user) {
        Document doc = new Document()
                .append("username", user.getUsername())
                .append("email", user.getEmail())
                .append("fullName", user.getFullName())
                .append("lastLogin", user.getLastLogin());

        collection.updateOne(
                Filters.eq("_id", user.getId()),
                new Document("$set", doc)
        );
    }

    private User documentToUser(Document doc) {
        if (doc == null) return null;

        User user = new User();
        user.setId(doc.getObjectId("_id"));
        user.setUsername(doc.getString("username"));
        user.setEmail(doc.getString("email"));
        user.setPasswordHash(doc.getString("passwordHash"));
        user.setFullName(doc.getString("fullName"));
        user.setCreatedAt(doc.getDate("createdAt"));
        user.setLastLogin(doc.getDate("lastLogin"));
        return user;
    }
}
