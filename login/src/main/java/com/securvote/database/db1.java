package com.securvote.database;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.util.HashMap;



public class db1 {
    static Dotenv dotenv = Dotenv.configure()
            .filename("apiee.env")
            .load();
    static String uri = dotenv.get("API_KEY");

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    static {
        try {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("DB1");
            collection = database.getCollection("reglogin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void connect(){
        System.out.println("Connected");
    }

    public static boolean findUser(String secretCode) {
        Document query = new Document("secret_code", secretCode);
        Document foundDocument = collection.find(query).first();

        // Check if the document exists
        if (foundDocument != null) {
            // Check if the password field exists and is not null or empty
            String password = foundDocument.getString("password");
            if (password != null && !password.isEmpty() && !password.equals("newPassword")) {
                return false; // User exists and password is set
            }
        }

        return true; // User does not exist or password is not set
    }



    public static HashMap<String, String> getUser(String secretCode) {
        Document query = new Document("secret_code", secretCode);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null) {
            HashMap<String, String> userMap = new HashMap<>();
            for (String key : foundDocument.keySet()) {
                userMap.put(key, foundDocument.get(key).toString());
            }
            return userMap;
        } else {
            return null; // or an appropriate message or empty map
        }
    }


    public static String updatePassword(String secretCode, String newPassword) {
        Document query = new Document("secret_code", secretCode);
        Document update = new Document("$set", new Document("password", newPassword));

        UpdateResult result = collection.updateOne(query, update);

        if (result.getModifiedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }

    public static String addUser(String secretCode, String username, String password) {
        Document query = new Document("secret_code", secretCode);
        FindIterable<Document> result = collection.find(query);

        if (result.first() != null) {
            return "Duplicate";
        }
        Document newUser = new Document("secret_code", secretCode)
                .append("username", username)
                .append("password", password);

        collection.insertOne(newUser);
        return "Success";
    }


    public static String updateUsername(String secretCode, String newUsername) {
        Document query = new Document("secret_code", secretCode);
        Document update = new Document("$set", new Document("username", newUsername));

        UpdateResult result = collection.updateOne(query, update);

        if (result.getModifiedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }

    public static String deleteRecord(String secretCode) {
        Document query = new Document("secret_code", secretCode);
        DeleteResult result = collection.deleteOne(query);

        if (result.getDeletedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }
    public static void deleteAllDocuments() {
        // Perform the delete operation on all documents
        collection.deleteMany(new Document());

        // Print confirmation
        System.out.println("All existing documents in the collection have been deleted.");
    }

    public static void closeMongoClient() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }


}
