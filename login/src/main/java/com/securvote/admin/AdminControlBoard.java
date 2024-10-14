package com.securvote.admin;
import com.securvote.database.*;
import com.securvote.voting.Blockchain;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class AdminControlBoard {

    public static void startElections(){
        filemanager.deleteAllFiles();
        keys.generateStringKey();
        filemanager.writeToFile("ADMIN",keys.privateKey);
        Blockchain.saveBlockchainToFile("privateKeys/blockchain.dat");
        db2.storePublicKey("ADMIN",keys.publicKey);
        System.out.println("Admin private and public key stored. Blockchain initialised!");

    }
    public static void endElections(){
        filemanager.deleteAllFiles();
    }
    public static void deleteUserDetails(String seccode){
        db1.deleteRecord(seccode);
        db2.deleteRecord(seccode);
    }
    public static void delAllDetails(){
        db1.deleteAllDocuments();
        db2.deleteAllDocuments();
        db3.deleteAllDocuments();
        filemanager.deleteAllFiles();
        db2.addNewUser("ADMIN");
        db1.addUser("ADMIN","","adminpass1234");
        HashMap<String, String> admin = db2.getUserDetails("ADMIN");
        admin.put("password","adminpass1234");
        db2.setUserDetails("ADMIN",admin);
    }
    public static void addelection() {
        Scanner scanner = new Scanner(System.in);

        // Delete all documents before adding the new election
        db3.deleteAllDocuments();

        // Prompt the user for election details
        System.out.print("Please enter election name: ");
        String elecname = scanner.nextLine();

        System.out.print("Please enter election date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        // Prompt the user to input candidate details
        List<Document> candidates = new ArrayList<>();
        System.out.print("Please enter number of candidates: ");
        int no = scanner.nextInt();
        scanner.nextLine(); // Consume the leftover newline

        for (int i = 0; i < no; i++) {
            System.out.print("Enter name of candidate " + (i + 1) + ": ");
            String name = scanner.nextLine();

            System.out.print("Enter party of candidate " + (i + 1) + ": ");
            String party = scanner.nextLine();

            System.out.print("Enter unique code of candidate: " + (i + 1) + ": ");
            String details = scanner.nextLine();

            // Add candidate details to the list
            Document candidate = new Document("name", name)
                    .append("party", party)
                    .append("details", details);

            candidates.add(candidate);
        }

        // Call the function to add election
        String result = db3.addElection(elecname, date, candidates);
        System.out.println(result);

        scanner.close();
    }

    public static void addUserDetails(){

        Scanner scanner=new Scanner(System.in);
        System.out.print("Please enter secretcode: ");
        String secretcode = scanner.nextLine();
        db2.addNewUser(secretcode);

        HashMap<String, String> user = db2.getUserDetails(secretcode);

        System.out.print("Please enter first_name: ");

        String Fname = scanner.nextLine();

        System.out.print("Please enter last_name: ");
        String Lname = scanner.nextLine();

        System.out.print("Please enter email: ");
        String email = scanner.nextLine();

        System.out.print("Please enter phone: ");
        String phone = scanner.nextLine();

        user.put("first_name",Fname);
        user.put("last_name",Lname);
        user.put("username","");
        user.put("password","");
        user.put("email",email);
        user.put("phone",phone);
        db1.addUser(secretcode,"","");
        String bb =db2.setUserDetails(secretcode,user);
        db2.setVotedStatus(secretcode,false);

        if (bb.equals("Success"))
            System.out.println("Your Details have been Set.");
        else System.out.println("Error! ");
    }

    public static void displayMenu() {
        System.out.println("\n--- Election System Menu ---");
        System.out.println("1. Add Election");
        System.out.println("2. Start Elections");
        System.out.println("3. Add User Details");
        System.out.println("4. Delete User Details by Secret Code");
        System.out.println("5. End Elections");
        System.out.println("505. Delete All Values");
        System.out.println("6. Exit and proceed to results");

        System.out.print("Please select an option (1-6): ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.println("\n--- Add Election ---");
                    addelection();
                    exit(1);
                    break;
                case 2:
                    System.out.println("\n--- Start Elections ---");
                    startElections();
                    exit(2);
                    break;

                case 3:
                    System.out.println("\n--- Add User Details ---");
                    addUserDetails();
                    break;
                case 4:
                    System.out.println("\n--- Delete User Details ---");
                    System.out.print("Please enter the secret code to delete user details: ");
                    String secretCode = scanner.nextLine();
                    deleteUserDetails(secretCode);
                    break;
                case 5:
                    System.out.println("\n--- End Elections ---");
                    endElections();
                    break;
                case 505:
                    System.out.println("\n--- Deleting all Values ---");
                    delAllDetails();
                    break;
                case 6:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (choice != 6);

    }

    }


