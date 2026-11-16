package com.stylesync.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Singleton class for MongoDB database connection
 */
public class DatabaseConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String CONNECTION_STRING;
    private static final String DATABASE_NAME;

    static {
        CONNECTION_STRING = ConfigUtil.getProperty("mongodb.uri");
        DATABASE_NAME = ConfigUtil.getProperty("mongodb.database");
    }

    private DatabaseConnection() {
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DATABASE_NAME);
                System.out.println("Connected to MongoDB Atlas successfully!");
            } catch (Exception e) {
                System.err.println("Failed to connect to MongoDB: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return database;
    }

    public static boolean testConnection() {
        try {
            MongoDatabase db = getDatabase();
            if (db != null) {
                db.listCollectionNames().first();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Connection test failed: " + e.getMessage());
        }
        return false;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }
}
