package com.securvote.controller;

import com.securvote.database.*;

import com.securvote.registration.QPE;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import com.securvote.voting.Blockchain;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@SpringBootApplication
@RestController
public class restcontroller {
    private static final Logger log = LoggerFactory.getLogger(restcontroller.class);
    public static PrivateKey user_prik;
    public static PublicKey admin_pubk;
    public static boolean authorised = false;
    public static String secretid;
    public static String hashid;
    char login;
    char mailcheck = '.';

    /**
     * Endpoint to set secret ID and determine the action.
     *
     * @param id The secret ID to set.
     * @return ResponseEntity with status code based on existence in DB.
     * @throws QPE Custom exception for any errors.
     */

    @GetMapping("/api/registerUser")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestParam String username, @RequestParam String password) throws Exception {
        System.out.println("Starting registration for secret ID: " + secretid);

        if (mailcheck == 'F' || login != 'R' || mailcheck == '.') {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "E");
            errorResponse.put("message", "Error updating Username or Password. Mail check fail or Login mode not register mode.");
            return ResponseEntity.status(500).body(errorResponse);
        }

        String usernameStatus = db1.updateUsername(secretid, username);
        String passwordStatus = db1.updatePassword(secretid, password);

        if (!"Success".equals(usernameStatus) || !"Success".equals(passwordStatus)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "E");
            errorResponse.put("message", "Error updating Username or Password.");
            return ResponseEntity.status(500).body(errorResponse);
        }

        // Step 2: Store updated user details in db2
        HashMap<String, String> users = db2.getUserDetails(secretid);
        users.put("username", username);
        users.put("password", password);
        db1.updatePassword(secretid, password);
        db1.updateUsername(secretid, username);
        db2.setUserDetails(secretid, users);
        System.out.println("User Details:");
        for (String key : users.keySet()) {
            System.out.printf("%s: %s%n", key, users.get(key)); // Prints user details
        }

        // Step 3: Generate public/private key pair and hash ID
        keys.generateStringKey();
        PublicKey pubk = keys.publicKey;
        PrivateKey prik = keys.privateKey;
        String hash = encrypt.generateHash(keys.publicString, "sp2005"); // Password to be put in env file

        // Step 4: Store public key and hash ID in db2
        db2.storePublicKey(secretid, pubk);
        db2.storeHashID(secretid, hash);

        // Step 5: Save private key to a file
        filemanager.writeToFile(secretid, prik);
        hashid = hash;
        // Display the generated hash ID to the user
        System.out.println("Your Hash ID is: " + hash + ". Please store it for future use.");

        // Create response data
        Map<String, Object> response = new HashMap<>();
        response.put("status", "S");
        response.put("message", "Registration successful.");
        response.put("hashID", hash);
        response.put("username", username);

        // Return the response as JSON
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/setSecret")
    public ResponseEntity<HashMap<String, Object>> setSecretId(@RequestParam String id) throws QPE {
        secretid = id;
        HashMap<String, String> hsh = db1.getUser(secretid);

        HashMap<String, Object> response = new HashMap<>();

        if (hsh == null) {
            secretid = "";
            login = 'N';
            response.put("status", "N");
            return ResponseEntity.ok(response); // Not in DB
        }

        // If the username is empty or default
        if (hsh.get("username").equalsIgnoreCase("") || hsh.get("username").equalsIgnoreCase("username")) {
            login = 'R';
            response.put("status", "R");
            return ResponseEntity.ok(response); // Call registration
        }

        // Proceed to login
        login = 'L';
        response.put("status", "L");

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to authenticate a user's email.
     * This method checks if the provided email matches the one stored in the database for the current user.
     *
     * @param mail The email address provided by the user for authentication.
     * @return ResponseEntity<String> Returns a success message if the email matches, otherwise an error message.
     * @throws QPE Custom exception for handling authentication errors.
     */
//step one of registration
    @GetMapping("/api/setEmail")
    public ResponseEntity<HashMap<String, Object>> setEmail(@RequestParam String mail) throws QPE {
        System.out.println(mail + secretid);
        HashMap<String, String> users = db2.getUserDetails(secretid);
        HashMap<String, Object> response = new HashMap<>();
        String email = mail;
        if (login!='R') {
            response.put("status", "E");
            response.put("message", "Registration is not the mode selected. ");
            return ResponseEntity.ok(response); // FAILURE
        }
        // Create response map


        // If email doesn't match, authentication fails
        if (!email.equals(users.get("email"))) {
            System.out.println("Error! Authentication failed.");
            mailcheck = 'F';
            response.put("status", "E");
            response.put("message", "failed email auth.");
            return ResponseEntity.ok(response); // Error
        }

        // If email matches, authentication is successful
        mailcheck = 'T';
        response.put("status", "S");
        response.put("message", "mail auth success");

        return ResponseEntity.ok(response); // Success
    }

    /**
     * Endpoint to handle user login.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return ResponseEntity with status code for success or failure.
     */
    @GetMapping("/api/login")
    public ResponseEntity<HashMap<String, Object>> loginUser(@RequestParam String username, @RequestParam String password) throws Exception {
        HashMap<String, String> hsh = db1.getUser(secretid);

        // Create response map
        HashMap<String, Object> response = new HashMap<>();

        int flag = 0;

        // Check if user exists and username matches
        if (hsh == null || !hsh.get("username").equals(username)) {
            flag = 1;
            response.put("status", "E");
            response.put("message", "Invalid username");
            return ResponseEntity.status(401).body(response);
        }

        // Check if the password matches
        if (!hsh.get("password").equals(password)) {
            flag = 1;
            response.put("status", "E");
            response.put("message", "Invalid password");
            return ResponseEntity.status(401).body(response);
        }

        // Check if the user has already voted
        boolean hasVoted = db2.getVotedStatus(secretid);
        if (hasVoted) {
            flag = 1;
            response.put("status", "E");
            response.put("message", "User has already voted");
            throw new QPE("AV"); // User has already voted
        }

        // Retrieve user's private key and admin's public key
        if (flag == 0) {
            user_prik = filemanager.retrievedContent(secretid);
            admin_pubk = db2.getPublicKey("ADMIN");
            authorised = true;
            hashid=db2.getHash(secretid);

            response.put("status", "S");
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        }

        response.put("status", "E");
        response.put("message", "Unknown error");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/getUserDetails")
    public ResponseEntity<HashMap<String, String>> getUserDetails(@RequestParam String secretid) {
        // Simulate fetching user details from the database
        HashMap<String, String> userDetails = db2.getUserDetails(secretid);
        userDetails.put("password","*******");
        // If user not found, return an error response
        if (userDetails == null) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
        System.out.println(userDetails);

        // Return the user details as JSON
        return ResponseEntity.ok(userDetails); // Success
    }

    @GetMapping("/api/getElections")
    public ResponseEntity<List<Map<String, Object>>> getElections() {
        List<Document> elections = db3.viewElections();  // Fetch elections from DB
        List<Map<String, Object>> response = new ArrayList<>();
        if (elections == null) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
        for (Document election : elections) {
            Map<String, Object> electionData = electionToMap(election);
            response.add(electionData);
        }

        // Return the list of election details as JSON
        return ResponseEntity.ok(response);
    }

    /**
     * Convert the election document to a map.
     * This is to ensure it can be returned as a proper JSON response.
     *
     * @param election The election document from the database.
     * @return A map representation of the election.
     */
    private Map<String, Object> electionToMap(Document election) {
        Map<String, Object> electionMap = new HashMap<>();

        // Convert each entry in the Document to a Map entry
        for (Map.Entry<String, Object> entry : election.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // If there are candidates, format them nicely
            if (key.equals("candidates")) {
                List<Document> candidateList = (List<Document>) value;
                List<Map<String, Object>> formattedCandidates = new ArrayList<>();
                for (Document candidate : candidateList) {
                    Map<String, Object> candidateData = candidateToMap(candidate);
                    formattedCandidates.add(candidateData);
                }
                electionMap.put("candidates", formattedCandidates);
            } else {
                electionMap.put(key, value);
            }
        }

        return electionMap;
    }

    /**
     * Convert candidate document to a map.
     * This is to ensure candidates are returned in a proper JSON format.
     *
     * @param candidate The candidate document from the election.
     * @return A map representation of the candidate.
     */
    private Map<String, Object> candidateToMap(Document candidate) {
        Map<String, Object> candidateMap = new HashMap<>();

        // Convert each entry in the candidate Document to a Map entry
        for (Map.Entry<String, Object> entry : candidate.entrySet()) {
            candidateMap.put(entry.getKey(), entry.getValue());
        }

        return candidateMap;
    }

    @GetMapping("/api/castVote")
    public ResponseEntity<HashMap<String, Object>> castVote(@RequestParam String vote) {
        HashMap<String, Object> response = new HashMap<>();
        System.out.println(vote);
        if (!authorised){
            response.put("status", "E");
            response.put("message", "not logged in");
            return ResponseEntity.ok(response);
        }
        try {
            // Convert vote to uppercase to standardize
            vote=vote.toUpperCase();

            String encryptedVote = encrypt.encryptString(vote,admin_pubk);
            String signature = encrypt.generateHash(secretid, db2.getUserDetails("ADMIN").get("password"));
            System.out.println("Hi"+encryptedVote+signature);
            String cnfm = db2.setVotedStatus(secretid, true);
            if (cnfm.equalsIgnoreCase("Failure")) throw new QPE("error! TRY AGAIN.");

            Blockchain.loadBlockchainFromFile("privateKeys/blockchain.dat");
            Blockchain.addBlock(encryptedVote, hashid, signature);
            Blockchain.saveBlockchainToFile("privateKeys/blockchain.dat");
            // Prepare success response
            response.put("status", "S");
            response.put("message", "Your vote has been recorded successfully!");

            System.out.println("Your vote has been recorded successfully! ");
        } catch (Exception e) {
            response.put("status", "E");
            response.put("message", e.getMessage());
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }


        public static void main(String[] args) {
        SpringApplication.run(restcontroller.class, args);
    }
}
