package com.peerlearn.database;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Projections;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.*;
import java.util.Random;



public class db_user {
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
            collection = database.getCollection("user");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean setUsernamePassword(String uname, String password) {

        Document doc = new Document("_id", new ObjectId());
        Random random = new Random();
        doc.append("uname", uname)
                .append("password", password)
                .append("stars", 4)
                .append("reputation", 0)
                .append("pinned_courses", Arrays.asList("2001"))
                .append("u_id", uname.substring(1,3)+String.valueOf(random.nextInt(100)))
                .append("course_learnt", Arrays.asList(
                        new Document("course_name", "Spring Boot").append("completion", 85).append("course_id", 2001).append("modules_purchased", Arrays.asList("Module1", "Module2")).append("completed_modules", Arrays.asList(1, 2)),
                        new Document("course_name", "MongoDB Basics").append("completion", 100).append("course_id", 2002).append("modules_purchased", Arrays.asList("ModuleA", "ModuleB")).append("completed_modules", Arrays.asList(1, 2, 3))
                ))
                .append("modules_purchased", Arrays.asList())
                .append("completed_modules", Arrays.asList())
                .append("courses_taught", Arrays.asList(new Document("course_name", "Java Basics").append("course_id", 3001)))
                .append("amount", 2500)
                .append("top_1_percent", 0)
                .append("top_10_percent", 0);

        collection.insertOne(doc);
        return true;
    }
    public static Map<String, Object> verifyUserCredentials(String username, String password) {
        Document user = collection.find(and(eq("uname", username), eq("password", password))).first();

        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            response.put("login", true);
            response.put("u_id", user.getString("u_id"));
        } else {
            response.put("login", false);
            response.put("u_id", -1);
        }
        return response;
    }
    public static List<Map<String, Object>> getPinnedCourses(String u_id) {
        // Fetch the user's pinned course IDs
        Bson projectionFields = Projections.fields(
                Projections.include("pinned_courses"),
                Projections.excludeId()
        );

        Document user = collection.find(eq("u_id", u_id)).projection(projectionFields).first();
        List<Map<String, Object>> pinnedCourses = new ArrayList<>();

        if (user != null && user.containsKey("pinned_courses")) {
            List<String> courseIds = user.getList("pinned_courses", String.class); // Directly fetch list of strings

            MongoCollection<Document> coursesCollection = database.getCollection("courses");

            int count = 0;
            for (String courseId : courseIds) {
                if (count >= 3) break; // Limit to 3 courses

                Document course = coursesCollection.find(eq("course_id", Integer.parseInt(courseId))).first();
                if (course != null) {
                    Map<String, Object> courseData = new HashMap<>();
                    courseData.put("course_name", course.getString("course_name"));
                    courseData.put("course_id", course.getInteger("course_id"));
                    courseData.put("completion", 0); // Default to 0 as completion data is missing in user document

                    pinnedCourses.add(courseData);
                    count++;
                }
            }
        }
        return pinnedCourses;
    }





}