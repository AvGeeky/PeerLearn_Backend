package com.securvote.database;

import com.mongodb.client.*;
import com.mongodb.client.result.UpdateResult;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;

import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import org.bson.types.Binary;
import java.security.PublicKey;
import java.util.List;

public class db2 {

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
            database = mongoClient.getDatabase("DB2");
            collection = database.getCollection("hashid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<Document> getAllHashAndVotedStatus() {
        List<Document> resultList = new ArrayList<>();

        // Fetch only the "hash_id" and "voted_status" fields
        FindIterable<Document> documents = collection.find().projection(new Document("hash_id", 1).append("voted_status", 1));

        // Iterate over the documents and add them to the resultList
        for (Document doc : documents) {
            String secretCode = doc.getString("hash_id");

            // Check if voted_status is of type Boolean or String
            Object votedStatusObj = doc.get("voted_status");
            boolean votedStatus = false; // Default value

            if (votedStatusObj instanceof Boolean) {
                votedStatus = (Boolean) votedStatusObj;
            } else if (votedStatusObj instanceof String) {
                votedStatus = Boolean.parseBoolean((String) votedStatusObj);
            }

            Document result = new Document("hash_id", secretCode).append("voted_status", votedStatus);
            resultList.add(result);
        }

        return resultList;
    }

    public static void connect(){
        System.out.println("Connected!");
    }
    // Method to set user details
    public static String setUserDetails(String secretCode, HashMap<String, String> userDetails) {
        Document query = new Document("secret_code", secretCode);
        Document update = new Document("$set", new Document("user_details", userDetails));

        UpdateResult result = collection.updateOne(query, update);

        if (result.getModifiedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }
    public static String addNewUser(String secretCode) {
        // Check if the secret_code already exists
        Document query = new Document("secret_code", secretCode);
        FindIterable<Document> result = collection.find(query);

        if (result.first() != null) {
            return "Secret code already exists.";
        }

        // Create a new document with only the secret_code
        Document newUser = new Document("secret_code", secretCode)
                .append("user_details", new Document())  // Empty user_details
                .append("public_key", "")                // Empty public key
                .append("voted_status", "")
                .append("hash_id","");             // Empty voted status

        // Insert the new document into the collection
        collection.insertOne(newUser);
        return "Secret code added successfully.";
    }

    public static String getSecretCodeByHashId(String hashId) {
        Document query = new Document("hash_id", hashId);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null) {
            return foundDocument.getString("secret_code");
        } else {
            return null; // Return null if no document found
        }
    }

    // Method to get user details
    public static HashMap<String, String> getUserDetails(String secretCode) {
        Document query = new Document("secret_code", secretCode);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null && foundDocument.containsKey("user_details")) {
            Document userDetailsDoc = (Document) foundDocument.get("user_details");

            HashMap<String, String> userDetailsMap = new HashMap<>();
            for (String key : userDetailsDoc.keySet()) {
                Object value = userDetailsDoc.get(key);
                if (value != null) {
                    userDetailsMap.put(key, value.toString()); // Convert the value to String
                } else {
                    userDetailsMap.put(key, null); // Optionally handle null values
                }
            }

            return userDetailsMap;
        } else {
            return new HashMap<>(); // Return an empty map if no user details found
        }
    }



    // Method to modify Public Key
    public static String storePublicKey(String secretCode, PublicKey publicKey) {
        Document query = new Document("secret_code", secretCode);
        byte[] publicKeyBytes = publicKey.getEncoded(); // Convert PublicKey to byte array
        Document update = new Document("$set", new Document("public_key", new Binary(publicKeyBytes)));

        UpdateResult result = collection.updateOne(query, update);

        if (result.getModifiedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }

    // Method to store HMAC
    public static String storeHashID(String secretCode, String hmac) {
        Document query = new Document("secret_code", secretCode);
        Document update = new Document("$set", new Document("hash_id", hmac));

        UpdateResult result = collection.updateOne(query, update);

        if (result.getModifiedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }
    // Method to get Public Key
    public static PublicKey getPublicKey(String secretCode) throws Exception {
        Document query = new Document("secret_code", secretCode);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null && foundDocument.containsKey("public_key")) {
            byte[] publicKeyBytes = foundDocument.get("public_key", Binary.class).getData();
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(spec);
        } else {
            return null; // or throw an exception
        }
    }

    // Method to get HMAC
    public static String getHash(String secretCode) {
        Document query = new Document("secret_code", secretCode);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null && foundDocument.containsKey("hash_id")) {
            return foundDocument.getString("hash_id"); // Directly retrieve as String
        } else {
            return null; // or throw an exception if you prefer
        }
    }


    // Method to change voted status
    public static String setVotedStatus(String secretCode, boolean voted) {
        Document query = new Document("secret_code", secretCode);
        Document update = new Document("$set", new Document("voted_status", voted));

        UpdateResult result = collection.updateOne(query, update);

        if (result.getModifiedCount() > 0) {
            return "Success";
        } else {
            return "Failure";
        }
    }

    // Method to get voted status
    public static boolean getVotedStatus(String secretCode) {
        Document query = new Document("secret_code", secretCode);
        Document foundDocument = collection.find(query).first();

        if (foundDocument != null && foundDocument.containsKey("voted_status")) {
            return foundDocument.getBoolean("voted_status", false);
        } else {
            return false; // default to false if not found
        }
    }
    public static void deleteRecord(String secretCode) {
        // Build the query to match the secret_code
        Document query = new Document("secret_code", secretCode);

        // Delete all documents that match the query
        collection.deleteMany(query);
        System.out.println("All documents with secret_code: " + secretCode + " have been deleted.");
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
