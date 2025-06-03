package com.peerlearn.controller;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.reflect.Array.set;

@SpringBootApplication
@CrossOrigin(origins = {"*"})
@RestController
public class RestController_v2 {
    static Dotenv dotenv = Dotenv.configure()
            .filename("apiee.env")
            .load();
    static String uri = dotenv.get("API_KEY");

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collectionUser;
    private static MongoCollection<Document> collectionCourses;
    private static MongoCollection<Document> collectionRoadmaps;

    static {
        try {
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri + "/?serverSelectionTimeoutMS=60000"))
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("PeerLearn");
            collectionUser = database.getCollection("user");
            collectionCourses = database.getCollection("courses");
            collectionRoadmaps = database.getCollection("roadmaps");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1. Set Credentials: Verify user login
     * API: /setCredentials
     */
    @PostMapping("/setCredentials")
    public ResponseEntity<Map<String, Object>> setCredentials(@RequestParam String username, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        Document user = collectionUser.find(eq("uname", username)).first();

        if (user != null && user.getString("password").equals(password)) {
            response.put("status", "S");
            response.put("message", "User verified successfully");
            response.put("login", true);
            response.put("u_id", user.getInteger("u_id"));
            return ResponseEntity.ok(response);
        }

        response.put("status", "E");
        response.put("message", "Invalid username or password");
        response.put("login", false);
        return ResponseEntity.status(401).body(response);
    }

    /**
     * 2. Get Pinned Courses
     * API: /getPinnedCourses
     */
    @GetMapping("/getPinnedCourses")
    public ResponseEntity<Map<String, Object>> getPinnedCourses(@RequestParam int u_id) {
        Map<String, Object> response = new HashMap<>();

        Document user = collectionUser.find(eq("u_id", u_id)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        List<Integer> pinnedCourses = user.getList("pinned_courses", Integer.class);
        if (pinnedCourses == null || pinnedCourses.isEmpty()) {
            response.put("status", "E");
            response.put("message", "No pinned courses found");
            return ResponseEntity.status(404).body(response);
        }

        List<Document> coursesData = collectionCourses.find(new Document("course_id", new Document("$in", pinnedCourses))).into(new ArrayList<>());

        response.put("status", "S");
        response.put("message", "Pinned courses retrieved successfully");
        response.put("pinned_courses", coursesData);
        return ResponseEntity.ok(response);
    }

    /**
     * 3. Get User Details
     * API: /getUserDetails
     */
    @GetMapping("/getUserDetails")
    public ResponseEntity<Map<String, Object>> getUserDetails(@RequestParam int u_id) {
        Map<String, Object> response = new HashMap<>();

        Document user = collectionUser.find(eq("u_id", u_id)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        response.put("status", "S");
        response.put("message", "User details retrieved successfully");
        response.put("uname", user.getString("uname"));

        // Handle potential Double values
        response.put("reputation", user.containsKey("reputation") ? ((Number) user.get("reputation")).intValue() : 0);
        response.put("stars", user.containsKey("stars") ? ((Number) user.get("stars")).intValue() : 0);
        response.put("amount", user.containsKey("amount") ? ((Number) user.get("amount")).intValue() : 0);
        response.put("top_1_percent", user.containsKey("top_1_percent") ? ((Number) user.get("top_1_percent")).intValue() : 0);
        response.put("top_10_percent", user.containsKey("top_10_percent") ? ((Number) user.get("top_10_percent")).intValue() : 0);

        response.put("course_learnt", user.get("course_learnt"));
        response.put("courses_taught", user.get("courses_taught"));

        return ResponseEntity.ok(response);
    }

    /**
     * 4. Get Top Courses
     * API: /getTopCourses
     */
    @GetMapping("/getTopCourses")
    public ResponseEntity<Map<String, Object>> getTopCourses() {
        Map<String, Object> response = new HashMap<>();

        FindIterable<Document> topCourses = collectionCourses.find()
                .sort(new Document("ratings", -1)) // Sort by ratings in descending order
                .limit(10); // Limit to top 10 courses

        List<Map<String, Object>> coursesList = new ArrayList<>();
        for (Document course : topCourses) {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("course_id", course.getInteger("course_id"));
            courseData.put("course_name", course.getString("course_name"));
            courseData.put("ratings", course.getDouble("ratings"));
            courseData.put("no_of_people_rated", course.getInteger("no_of_people_rated"));
            coursesList.add(courseData);
        }

        response.put("status", "S");
        response.put("message", "Top 10 courses retrieved successfully");
        response.put("top_courses", coursesList);

        return ResponseEntity.ok(response);
    }
    /**
     * 5. Get Completed Courses
     * API: /getCompletedCourses
     */
    @GetMapping("/getCompletedCourses")
    public ResponseEntity<Map<String, Object>> getCompletedCourses(@RequestParam int u_id) {
        Map<String, Object> response = new HashMap<>();

        // Cast `int` to `double`
        Double u_idDouble = (double) u_id;

        Document user = collectionUser.find(eq("u_id", u_idDouble)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        List<Document> coursesLearnt = user.getList("course_learnt", Document.class);
        if (coursesLearnt == null || coursesLearnt.isEmpty()) {
            response.put("status", "E");
            response.put("message", "No completed courses found");
            return ResponseEntity.status(404).body(response);
        }

        List<Map<String, Object>> completedCourses = new ArrayList<>();
        for (Document course : coursesLearnt) {
            Map<String, Object> courseData = new HashMap<>();
            courseData.put("course_name", course.getString("course_name"));
            courseData.put("course_id", course.getInteger("course_id"));
            courseData.put("completion", course.getInteger("completion"));
            completedCourses.add(courseData);
        }

        response.put("status", "S");
        response.put("message", "Completed courses retrieved successfully");
        response.put("completed_courses", completedCourses);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getCoursesTaught")
    public ResponseEntity<Map<String, Object>> getCoursesTaught(@RequestParam int u_id) {
        Map<String, Object> response = new HashMap<>();

        Double u_idAsDouble = (double) u_id;
        Document user = collectionUser.find(eq("u_id", u_idAsDouble)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        List<Object> coursesTaughtObjects = user.getList("courses_taught", Object.class);

        if (coursesTaughtObjects == null || coursesTaughtObjects.isEmpty()) {
            response.put("status", "E");
            response.put("message", "No courses taught found");
            return ResponseEntity.status(404).body(response);
        }


        response.put("status", "S");
        response.put("message", "Courses taught retrieved successfully");
        response.put("courses_taught", coursesTaughtObjects);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/getStarsEarned")
//    public ResponseEntity<Map<String, Object>> getStarsEarned(@RequestParam int u_id) {
//        Map<String, Object> response = new HashMap<>();
//
//        Double u_idAsDouble = (double) u_id;
//        Document user = collectionUser.find(eq("u_id", u_idAsDouble)).first();
//        if (user == null) {
//            response.put("status", "E");
//            response.put("message", "User not found");
//            return ResponseEntity.status(404).body(response);
//        }
//
//        int starsEarned = user.getInteger("stars", 0);
//
//        response.put("status", "S");
//        response.put("message", "Stars earned retrieved successfully");
//        response.put("stars_earned", starsEarned);
//        return ResponseEntity.ok(response);
//    }
// Global conversion rate (1 star = X currency units)
private static final double STAR_CONVERSION_RATE = 0.1; // Adjust as needed

    /**
     * 8. Redeem Stars
     * API: /redeemStars
     */
    @PostMapping("/redeemStars")
    public ResponseEntity<Map<String, Object>> redeemStars(@RequestParam int u_id, @RequestParam int stars) {
        Map<String, Object> response = new HashMap<>();

        Document user = collectionUser.find(eq("u_id", u_id)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        int currentStars = user.getInteger("stars", 0);
        if (stars > currentStars) {
            response.put("status", "E");
            response.put("message", "Insufficient stars");
            return ResponseEntity.status(400).body(response);
        }

        // Calculate amount to be added
        double amountToAdd = stars * STAR_CONVERSION_RATE;

        // Update user's stars and balance
        Bson updateFields = new Document("$inc", new Document("amount", amountToAdd)
                .append("stars", -stars));
        collectionUser.updateOne(eq("u_id", u_id), updateFields);

        response.put("status", "S");
        response.put("message", "Stars redeemed successfully");
        response.put("redeemed_stars", stars);
        response.put("amount_added", amountToAdd);
        return ResponseEntity.ok(response);
    }
//    @GetMapping("/createCourse")
//    public ResponseEntity<Map<String, Object>> createCourse(
//            @RequestParam String course_name,
//            @RequestParam List<String> tags,
//            @RequestParam int u_id) {
//
//        Map<String, Object> response = new HashMap<>();
//        int user_id=u_id;
//        // Generate a unique course_id (incremental)
//        Document lastCourse = collectionCourses.find()
//                .sort(new Document("course_id", -1))
//                .limit(1)
//                .first();
//        int newCourseId = (lastCourse != null) ? lastCourse.getInteger("course_id") + 1 : 3001;
//
//        // Create new course document
//        Document newCourse = new Document()
//                .append("course_id", newCourseId)
//                .append("course_name", course_name)
//                .append("user_id", user_id)
//                .append("stars_earned", 0)
//                .append("ratings", 0.0)
//                .append("no_of_people_rated", 0)
//                .append("total_cost", 0)
//                .append("tags", tags)
//                .append("modules", new ArrayList<>());
//
//        // Insert course into database
//        collectionCourses.insertOne(newCourse);
//
//        response.put("status", "S");
//        response.put("message", "Course created successfully");
//        response.put("course_id", newCourseId);
//
//        return ResponseEntity.ok(response);
//    }
//    @GetMapping("/addModule")
//    public ResponseEntity<Map<String, Object>> addModule(
//            @RequestParam int course_id,
//            @RequestParam String module_name,
//            @RequestParam int module_cost,
//            @RequestParam String quiz_link) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        // Find the course
//        Document course = collectionCourses.find(eq("course_id", course_id)).first();
//        if (course == null) {
//            response.put("status", "E");
//            response.put("message", "Course not found");
//            return ResponseEntity.status(404).body(response);
//        }
//
//        // Create module document
//        Document newModule = new Document()
//                .append("Modulename", module_name)
//                .append("lesson_name", "") // Placeholder for lesson name
//                .append("youtube_link", "") // Placeholder for YouTube link
//                .append("modulecost", module_cost)
//                .append("quiz_link", quiz_link);
//
//        // Update course with new module
//        List<Document> modules = course.getList("modules", Document.class, new ArrayList<>());
//        modules.add(newModule);
//
//        collectionCourses.updateOne(eq("course_id", course_id), new Document("$set", new Document("modules", modules)));
//
//        response.put("status", "S");
//        response.put("message", "Module added successfully");
//
//        return ResponseEntity.ok(response);
//    }
//@GetMapping("/createCourse")
//public ResponseEntity<Map<String, Object>> createCourse(
//        @RequestParam String course_name,
//        @RequestParam List<String> tags,
//        @RequestParam int user_id) {
//
//    Map<String, Object> response = new HashMap<>();
//
//    // Generate unique course_id
//    int course_id = new Random().nextInt(9000) + 1000; // Random 4-digit ID
//
//    Document newCourse = new Document()
//            .append("course_id", course_id)
//            .append("course_name", course_name)
//            .append("user_id", user_id)
//            .append("stars_earned", 0)
//            .append("ratings", 0.0)
//            .append("no_of_people_rated", 0)
//            .append("total_cost", 0)
//            .append("tags", tags)
//            .append("modules", new ArrayList<>()); // Empty modules list
//
//    collectionCourses.insertOne(newCourse);
//
//    response.put("status", "S");
//    response.put("message", "Course created successfully");
//    response.put("course_id", course_id);
//
//    return ResponseEntity.ok(response);
//}
@GetMapping("/createCourse")
public ResponseEntity<Map<String, Object>> createCourse(
        @RequestParam String course_name,
        @RequestParam List<String> tags,
        @RequestParam int user_id) {

    Map<String, Object> response = new HashMap<>();

    // Generate unique course_id
    int course_id = new Random().nextInt(9000) + 1000; // Random 4-digit ID

    // Create new course document
    Document newCourse = new Document()
            .append("course_id", course_id)
            .append("course_name", course_name)
            .append("user_id", user_id)
            .append("stars_earned", 0)
            .append("ratings", 0.0)
            .append("no_of_people_rated", 0)
            .append("total_cost", 0)
            .append("tags", tags)
            .append("modules", new ArrayList<>()); // Empty modules list

    collectionCourses.insertOne(newCourse);

    // Find the user document
    Document user = collectionUser.find(eq("u_id", user_id)).first();

    if (user != null) {
        // Get existing courses_taught list or initialize if null
        List<Document> coursesTaught = user.getList("courses_taught", Document.class, new ArrayList<>());

        // Add new course to courses_taught
        Document newCourseEntry = new Document()
                .append("course_id", course_id)
                .append("course_name", course_name);

        coursesTaught.add(newCourseEntry);

        // Update user document
        collectionUser.updateOne(eq("u_id", user_id), new Document("$set", new Document("courses_taught", coursesTaught)));
    }

    response.put("status", "S");
    response.put("message", "Course created successfully and added to user’s courses_taught");
    response.put("course_id", course_id);

    return ResponseEntity.ok(response);
}

    public boolean updateTotalCost(int courseId, int totalCost) {
        // Find the course by course_id
        Document course = collectionCourses.find(eq("course_id", courseId)).first();

        if (course == null) {
            System.out.println("Course not found");
            return false;
        }

        // Update the total_cost field
        collectionCourses.updateOne(eq("course_id", courseId), Updates.set("total_cost", totalCost));

        System.out.println("Total cost updated successfully for course_id: " + courseId);
        return true;
    }
    @PostMapping("/createModule")
    public ResponseEntity<Map<String, Object>> createModule(
            @RequestParam int course_id,
            @RequestParam String module_name,
            @RequestParam int module_cost,
            @RequestParam String quiz_link) {

        Map<String, Object> response = new HashMap<>();

        // Find the course
        Document course = collectionCourses.find(eq("course_id", course_id)).first();
        if (course == null) {
            response.put("status", "E");
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }
        updateTotalCost(course_id,module_cost);
        // Create a module with an empty lessons list
        Document newModule = new Document()
                .append("Modulename", module_name)
                .append("modulecost", module_cost)
                .append("quiz_link", quiz_link)
                .append("lesson_name", new ArrayList<>()); // Empty lessons list

        // Add the module to the course
        List<Document> modules = course.getList("modules", Document.class, new ArrayList<>());
        modules.add(newModule);

        collectionCourses.updateOne(eq("course_id", course_id), new Document("$set", new Document("modules", modules)));

        response.put("status", "S");
        response.put("message", "Module added successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/createLesson")
    public ResponseEntity<Map<String, Object>> createLesson(
            @RequestParam int course_id,
            @RequestParam String module_name,
            @RequestParam String lesson_link) {

        Map<String, Object> response = new HashMap<>();

        // Find the course
        Document course = collectionCourses.find(eq("course_id", course_id)).first();
        if (course == null) {
            response.put("status", "E");
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }

        List<Document> modules = course.getList("modules", Document.class, new ArrayList<>());
        boolean moduleFound = false;

        for (Document module : modules) {
            if (module.getString("Modulename").equals(module_name)) {
                List<String> lessons = module.getList("lesson_name", String.class, new ArrayList<>());
                lessons.add(lesson_link);

                module.put("lesson_name", lessons);
                moduleFound = true;
                break;
            }
        }

        if (!moduleFound) {
            response.put("status", "E");
            response.put("message", "Module not found");
            return ResponseEntity.status(404).body(response);
        }

        collectionCourses.updateOne(eq("course_id", course_id), new Document("$set", new Document("modules", modules)));

        response.put("status", "S");
        response.put("message", "Lesson added successfully");

        return ResponseEntity.ok(response);
    }
    @PostMapping("/buyStars")
    public ResponseEntity<Map<String, Object>> buyStars(@RequestParam int u_id, @RequestParam double amount) {
        Map<String, Object> response = new HashMap<>();

        double STAR_CONVERSION_RATE = 10.0; // 1 star = 10 currency units (example)

        Document user = collectionUser.find(eq("u_id", u_id)).first();
        System.out.println(user);
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        int starsToAdd = (int) (amount / STAR_CONVERSION_RATE);
        int currentStars = user.getInteger("stars", 0);

        // Fix: Use getDouble instead of getInteger
        double currentBalance = user.getDouble("amount");

        if (amount > currentBalance) {
            response.put("status", "E");
            response.put("message", "Insufficient balance");
            return ResponseEntity.status(400).body(response);
        }

        collectionUser.updateOne(
                eq("u_id", u_id),
                Updates.combine(
                        Updates.set("stars", currentStars + starsToAdd),
                        Updates.set("balance", currentBalance - amount) // Ensure correct field name
                )
        );

        response.put("status", "S");
        response.put("message", "Stars purchased successfully");
        response.put("stars_added", starsToAdd);
        response.put("new_balance", currentBalance - amount);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getCoursesByName")
    public ResponseEntity<Map<String, Object>> getCoursesByName(@RequestParam String course_name) {
        Map<String, Object> response = new HashMap<>();

        List<Document> courses = collectionCourses.find(eq("course_name", course_name)).into(new ArrayList<>());
        if (courses.isEmpty()) {
            response.put("status", "E");
            response.put("message", "No courses found with the given name");
            return ResponseEntity.status(404).body(response);
        }

        List<Map<String, Object>> courseList = new ArrayList<>();
        for (Document course : courses) {
            Map<String, Object> courseData = new HashMap<>();
            int u_id = course.getInteger("user_id");
            Document teacher = collectionUser.find(eq("u_id", u_id)).first();

            courseData.put("course_name", course.getString("course_name"));
            courseData.put("course_id", course.getInteger("course_id"));
            courseData.put("teacher_name", (teacher != null) ? teacher.getString("uname") : "Unknown");
            courseData.put("reputation", (teacher != null) ? teacher.getInteger("reputation", 0) : 0);
            courseData.put("no_of_modules", course.getList("Modules", Document.class, new ArrayList<>()).size());
            courseData.put("total_cost", course.getInteger("total_cost", 0));

            // Correct retrieval of ratings
            Object ratingObj = course.get("ratings");
            double ratings = (ratingObj instanceof Number) ? ((Number) ratingObj).doubleValue() : 0.0;
            courseData.put("ratings", ratings);

            courseList.add(courseData);
        }

        response.put("status", "S");
        response.put("message", "Courses retrieved successfully");
        response.put("courses", courseList);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/getCoursesByTags")
    public ResponseEntity<Map<String, Object>> getCoursesByTags(@RequestParam List<String> tags) {
        Map<String, Object> response = new HashMap<>();

        // Find courses that contain at least one of the provided tags
        List<Document> courses = collectionCourses.find(Filters.in("tags", tags)).into(new ArrayList<>());
        if (courses.isEmpty()) {
            response.put("status", "E");
            response.put("message", "No courses found with the given tags");
            return ResponseEntity.status(404).body(response);
        }

        List<Map<String, Object>> courseList = new ArrayList<>();
        for (Document course : courses) {
            Map<String, Object> courseData = new HashMap<>();

            // Correct field name: Fetch "user_id" instead of "u_id"
            Integer uidObj = course.getInteger("user_id");
            int u_id = (uidObj != null) ? uidObj : -1;  // Default to -1 if missing

            // Fetch teacher details
            Document teacher = collectionUser.find(eq("u_id", u_id)).first();

            courseData.put("course_name", course.getString("course_name"));

            Integer courseIdObj = course.getInteger("course_id");
            courseData.put("course_id", (courseIdObj != null) ? courseIdObj : 0);

            courseData.put("teacher_name", (teacher != null) ? teacher.getString("uname") : "Unknown");

            Integer reputationObj = (teacher != null) ? teacher.getInteger("reputation") : null;
            courseData.put("reputation", (reputationObj != null) ? reputationObj : 0);

            courseData.put("no_of_modules", course.getList("modules", Document.class, new ArrayList<>()).size());

            Integer totalCostObj = course.getInteger("total_cost");
            courseData.put("total_cost", (totalCostObj != null) ? totalCostObj : 0);

            // Correct retrieval of ratings
            Object ratingObj = course.get("ratings");
            double ratings = (ratingObj instanceof Number) ? ((Number) ratingObj).doubleValue() : 0.0;
            courseData.put("ratings", ratings);

            courseList.add(courseData);
        }

        response.put("status", "S");
        response.put("message", "Courses retrieved successfully");
        response.put("courses", courseList);

        return ResponseEntity.ok(response);
    }




    @GetMapping("/getAllPaidModules")
    public ResponseEntity<Map<String, Object>> getAllPaidModules(@RequestParam int u_id, @RequestParam int course_id) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve user document using u_id
        Document user = collectionUser.find(eq("u_id", u_id)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        // Retrieve courses where modules have been purchased
        List<Document> coursesLearned = user.getList("course_learnt", Document.class, new ArrayList<>());

        // Search for the given course_id in the user's learned courses
        for (Document course : coursesLearned) {
            if (course.getInteger("course_id") == course_id) {
                Map<String, Object> moduleData = new HashMap<>();
                moduleData.put("course_name", course.getString("course_name"));
                moduleData.put("course_id", course.getInteger("course_id"));

                // Retrieve purchased modules for the course
                List<String> purchasedModules = course.getList("modules_purchased", String.class, new ArrayList<>());
                moduleData.put("modules_purchased", purchasedModules);

                response.put("status", "S");
                response.put("message", "Purchased modules retrieved successfully");
                response.put("course_details", moduleData);

                return ResponseEntity.ok(response);
            }
        }

        // If course_id is not found in the user's learned courses
        response.put("status", "E");
        response.put("message", "Course not found in user's purchased list");
        return ResponseEntity.status(404).body(response);
    }
    @GetMapping("/getModuleNames")
    public ResponseEntity<Map<String, Object>> getModuleNames(@RequestParam int course_id) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve course document using course_id
        Document course = collectionCourses.find(eq("course_id", course_id)).first();
        if (course == null) {
            response.put("status", "E");
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }

        // Extract module names from the 'modules' array
        List<Document> modules = course.getList("modules", Document.class, new ArrayList<>());
        List<String> moduleNames = modules.stream()
                .map(module -> module.getString("Modulename"))
                .collect(Collectors.toList());

        response.put("status", "S");
        response.put("message", "Modules retrieved successfully");
        response.put("course_name", course.getString("course_name"));
        response.put("course_id", course_id);
        response.put("module_names", moduleNames);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getModuleDetails")
    public ResponseEntity<Map<String, Object>> getModuleDetails(@RequestParam int course_id, @RequestParam String module_name) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve course document using course_id
        Document course = collectionCourses.find(eq("course_id", course_id)).first();
        if (course == null) {
            response.put("status", "E");
            response.put("message", "Course not found");
            return ResponseEntity.status(404).body(response);
        }

        // Extract module details from the 'modules' array
        List<Document> modules = course.getList("modules", Document.class, new ArrayList<>());
        Document selectedModule = null;

        for (Document module : modules) {
            if (module_name.equals(module.getString("Modulename"))) {
                selectedModule = module;
                break;
            }
        }

        if (selectedModule == null) {
            response.put("status", "E");
            response.put("message", "Module not found in the given course");
            return ResponseEntity.status(404).body(response);
        }

        // Prepare module details response
        Map<String, Object> moduleData = new HashMap<>();
        moduleData.put("module_name", selectedModule.getString("Modulename"));
        moduleData.put("module_cost", selectedModule.getInteger("modulecost", 0));
        moduleData.put("quiz_link", selectedModule.getString("quiz_link"));
        moduleData.put("lesson_names", selectedModule.getList("lesson_name", String.class, new ArrayList<>()));

        response.put("status", "S");
        response.put("message", "Module details retrieved successfully");
        response.put("course_name", course.getString("course_name"));
        response.put("course_id", course_id);
        response.put("module", moduleData);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/addStars")
    public ResponseEntity<Map<String, Object>> addStars(@RequestParam int u_id, @RequestParam int stars) {
        Map<String, Object> response = new HashMap<>();

        // Find user document by u_id
        Document user = collectionUser.find(eq("u_id", u_id)).first();
        if (user == null) {
            response.put("status", "E");
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        // Get current stars and update
        int currentStars = user.getInteger("stars", 0);
        int newStarCount = currentStars + stars;

        collectionUser.updateOne(eq("u_id", u_id), Updates.set("stars", newStarCount));
        response.put("status", "S");
        response.put("message", "Stars added successfully");
        response.put("u_id", u_id);
        response.put("new_stars", newStarCount);

        return ResponseEntity.ok(response);
    }















    public static void main(String[] args) {
        SpringApplication.run(RestController_v2.class, args);
    }
}
