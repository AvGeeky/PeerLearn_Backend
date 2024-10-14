package com.securvote.voting;
import com.securvote.login.Login;
import com.securvote.database.*;
import com.securvote.registration.QPE;
import org.bson.Document;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.Scanner;

public class Voting {
    public static void displayElections(){
        List<Document> elections=db3.viewElections();

        System.out.println("Election Details:");
        for (Document election : elections) {
            for (String key : election.keySet()) {
                if (key.equals("candidates")) {
                    // Print candidates nicely
                    List<Document> candidateList = (List<Document>) election.get(key);
                    System.out.println(key + ":");
                    for (Document candidate : candidateList) {
                        System.out.println("  - Name: " + candidate.getString("name"));
                        System.out.println("    Party: " + candidate.getString("party"));
                        System.out.println("    Details: " + candidate.getString("details"));
                    }
                } else {
                    System.out.println(key + ": " + election.get(key));
                }
            }
            System.out.println();
            }
    }
    public static void selectElection(String electionname){
        List<Document> elections = db3.getElectionByName("Presidential Election 2024");
        System.out.println("You have selected "+electionname);
        for (Document election : elections) {
            for (String key : election.keySet()) {
                if (key.equals("candidates")) {
                    // Print candidates nicely
                    List<Document> candidateList = (List<Document>) election.get(key);
                    System.out.println(key + ":");
                    for (Document candidate : candidateList) {
                        System.out.println("  - Name: " + candidate.getString("name"));
                        System.out.println("    Party: " + candidate.getString("party"));
                        System.out.println("    Details: " + candidate.getString("details"));
                    }
                } else {
                    System.out.println(key + ": " + election.get(key));
                }
            }
            System.out.println();
        }
    }
    public static void main(String[] args) throws Exception {

        Login.main(args);
        boolean auth = Login.authorised;

        if (auth){
            Scanner scanner = new Scanner(System.in);

            String hashid = db2.getHash(Login.secretid);
            System.out.print("NOTE: Your HASH ID is: ");
            System.out.println(hashid);

            //HERE, YOU CAN ADD ACCEPT RULES AND REGULATIONS OF ELECTIONS SCREEN

            System.out.print("Do you agree to the rules "); //INPUT USERNAME
            String yn = scanner.nextLine();

            if (yn.equalsIgnoreCase("yes")){
                //prints election details
                displayElections();

                System.out.println("Enter the code of the candidate you want to vote for (CODE-A , CODE-B etc.): "); //CODE-A, CODE-B ETC
                String vote = scanner.nextLine();
                vote.toUpperCase();

                System.out.println("This is your entered code: ");
                System.out.println(vote);

                System.out.print("Do you agree to continue? "); //INPUT USERNAME
                String yesno = scanner.nextLine();

                if (yesno.equalsIgnoreCase("no")) throw new QPE("You have pressed no!");

                String encryptedVote=encrypt.encryptString(vote,Login.admin_pubk);
                String signature = encrypt.generateHash(Login.secretid,db2.getUserDetails("ADMIN").get("password"));

                String cnfm = db2.setVotedStatus(Login.secretid,true);
                if (cnfm.equalsIgnoreCase("Failure")) throw new QPE("error! TRY AGAIN.");

                Blockchain.loadBlockchainFromFile("privateKeys/blockchain.dat");
                Blockchain.addBlock(encryptedVote, hashid, signature);
                Blockchain.saveBlockchainToFile("privateKeys/blockchain.dat");

                System.out.println("Your vote has been recorded successfully! ");


            }

        }
    }
}
