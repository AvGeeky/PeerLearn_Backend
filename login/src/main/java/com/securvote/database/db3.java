package com.securvote.database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;

public class db3 {

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
            database = mongoClient.getDatabase("DB3");
            collection = database.getCollection("elections");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add an election
    public static String addElection(String electionName, String date, List<Document> candidates) {
        Document election = new Document("election_name", electionName)
                .append("date", date)
                .append("candidates", candidates);

        collection.insertOne(election);
        return "Election added successfully.";
    }
    public static List<Document> getElectionByName(String electionName) {
        Document query = new Document("election_name", electionName);
        Document foundElection = collection.find(query).first();
        List<Document> n = new ArrayList<>();
        n.add(foundElection);
        if (foundElection != null) {
            return n; // Return the found election
        } else {
            System.out.println("Election not found: " + electionName);
            return null; // Or handle as needed (e.g., return an empty document)
        }
    }

    // Method to view all elections
    public static List<Document> viewElections() {
        List<Document> electionsList = new ArrayList<>();
        for (Document doc : collection.find()) {
            electionsList.add(doc);
        }
        return electionsList;
    }

    // Method to add a candidate's details
    public static Document createCandidate(String name, String party, String details) {
        return new Document("name", name)
                .append("party", party)
                .append("details", details);
    }
    public static String getCandidateNameByCode(String code) {
        // Fetch the first election document in the collection
        Document election = collection.find().first(); // Assuming there's only one document

        if (election != null) {
            // Extract the candidates list from the document
            List<Document> candidates = (List<Document>) election.get("candidates");

            // Iterate through the candidates to find the matching code
            for (Document candidate : candidates) {
                if (candidate.getString("details").equals(code)) {
                    return candidate.getString("name"); // Return the candidate's name
                }
            }
        }

        return "Candidate not found"; // Return a message if the code is not found
    }
    public static void deleteAllDocuments() {
        // Perform the delete operation on all documents
        collection.deleteMany(new Document());

        // Print confirmation
        System.out.println("All existing documents in the collection have been deleted.");
    }

    }


