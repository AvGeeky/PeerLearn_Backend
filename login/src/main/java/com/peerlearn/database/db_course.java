package com.peerlearn.database;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

public class db_course {
    static Dotenv dotenv = Dotenv.configure()
            .filename("apiee.env")
            .load();
    static String uri = dotenv.get("API_KEY");

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    static {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri+"/?serverSelectionTimeoutMS=60000"))
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("PeerLearn");
            collection = database.getCollection("courses");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createCourse(int courseId, String courseName, int userId, int starsEarned, double ratings,
                                    int noOfPeopleRated, int totalCost, List<String> tags, List<Document> modules) {

        Document doc = new Document("_id", new ObjectId());
        doc.append("course_id", courseId)
                .append("course_name", courseName)
                .append("user_id", userId)
                .append("stars_earned", starsEarned)
                .append("ratings", ratings)
                .append("no_of_people_rated", noOfPeopleRated)
                .append("total_cost", totalCost)
                .append("tags", tags)
                .append("modules", modules);

        collection.insertOne(doc);
    }

    public static void main(String[] args) {
        createCourse(
                2001,
                "Spring Boot Masterclass",
                1001,
                4,
                4.5,
                250,
                1999,
                Arrays.asList("Spring Boot", "Java", "Backend Development"),
                Arrays.asList(
                        new Document("Modulename","Introduction to Spring Boot")
                                .append("lesson_name","Getting Started with Spring Boot")
                                .append("youtube_link","https://youtu.be/xyz123")
                                .append("modulecost",0)
                                .append("quiz_link","https://example.com/quiz1"),
                        new Document("Modulename","Spring Boot REST API")
                                .append("lesson_name","Building REST APIs with Spring Boot")
                                .append("youtube_link","https://youtu.be/abc456")
                                .append("modulecost",299)
                                .append("quiz_link","https://example.com/quiz2")
                ));
    }


}
