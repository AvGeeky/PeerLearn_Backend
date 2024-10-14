package com.securvote.admin;
import com.securvote.database.*;
import com.securvote.registration.QPE;
import com.securvote.voting.*;
import org.bson.Document;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Verification {
    public static boolean checkUniqueHash(List<Document> list) {
        Map<String, Integer> hashIdCount = new HashMap<>();
        Map<String, Boolean> votedStatusMap = new HashMap<>();

        // Count occurrences of each hash_id and store voted status
        for (Document doc : list) {
            String hashId = doc.getString("hash_id");
            Boolean votedStatus = doc.getBoolean("voted_status", false); // Default to false if voted_status is not present

            // Only consider non-empty hash_id
            if (hashId != null && !hashId.isEmpty()) {
                hashIdCount.put(hashId, hashIdCount.getOrDefault(hashId, 0) + 1);
                votedStatusMap.put(hashId, votedStatus);
            }
        }

        // Check for hash_id that occurs only once and return voted status
        for (Map.Entry<String, Integer> entry : hashIdCount.entrySet()) {
            String hashId = entry.getKey();
            int count = entry.getValue();

            if (count == 1) {
                continue;
            } else {
                System.out.println("Hash ID: " + hashId + " occurs more than once.");
            }
        }

        // If no hash_id occurs more than once, return true
        return !hashIdCount.containsValue(2);
    }
    public static List<String> getAllHashIds(List<Document> list) {
        List<String> hashIds = new ArrayList<>();

        // Iterate through the documents and add each hash_id to the list
        for (Document doc : list) {
            String hashId = doc.getString("hash_id");
            // Only add non-empty hash_id to the list
            if (hashId != null && !hashId.isEmpty()) {
                hashIds.add(hashId);
            }
        }

        return hashIds;
    }

    public static boolean authenticateBlock(VoteBlock vb, PublicKey pubk, String secretcode) throws Exception {
            boolean sig_decrypted = encrypt.verifyHash(secretcode,db2.getUserDetails("ADMIN").get("password"),vb.getSignature());
            if (!sig_decrypted) return false;
        System.out.println("Signature for "+secretcode+" authenticated.");
            return true;
    }
    public static Map<String, Integer> countVotes(List<String> votes) {
        Map<String, Integer> voteCountMap = new HashMap<>();

        for (String vote : votes) {
            voteCountMap.put(vote, voteCountMap.getOrDefault(vote, 0) + 1);
        }

        return voteCountMap;
    }
    public static void printVoteCounts(Map<String, Integer> voteCount) {
        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            System.out.println(entry.getKey() + " received " + entry.getValue() + " votes.");
        }
    }

    public static void main(String[] args) throws Exception {

        Blockchain.loadBlockchainFromFile("privateKeys/blockchain.dat");
        boolean blockchainValid = Blockchain.isChainValid();
        if (!blockchainValid) throw new QPE("THE BLOCKCHAIN HAS BEEN TAMPERED WITH.");

        List<Document> list = db2.getAllHashAndVotedStatus();
        boolean allUnique = checkUniqueHash(list);

        if (!allUnique) throw new QPE("THE DATABASE HAS BEEN TAMPERED WITH. THERE ARE NON UNIQUE MEMBERS!");

        System.out.println("Database and Blockchain checked. Both Intact!");

        List<String> allHashIds = getAllHashIds(list); //list of hashes
        List<String> votes = new ArrayList<>();
        List<String> uniquehash = new ArrayList<>();

        for (VoteBlock block:Blockchain.chain){
            if (block.getSignature().equals("GenesisSignature")) continue;
            String hash = block.getHashID();
            if (!uniquehash.contains(hash))
                uniquehash.add(hash);
            else throw new QPE("There are multiple votes for a single person! Error!");

            String secretCode = db2.getSecretCodeByHashId(hash);
            PublicKey pk = db2.getPublicKey(secretCode);
            if (!authenticateBlock(block,pk,secretCode)) throw new QPE("Signature not verified! Error!");
            String code = encrypt.decryptString(block.getVote(),filemanager.retrievedContent("ADMIN"));
            votes.add(db3.getCandidateNameByCode(code));
        }

        Map<String, Integer> voteCount = countVotes(votes);
        printVoteCounts(voteCount);













    }
}
