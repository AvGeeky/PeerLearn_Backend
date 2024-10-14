package com.securvote.voting;

import com.securvote.database.*;
import org.bson.Document;

import java.util.List;
import java.util.Scanner;

public class PublicViewPage {
    public static void main(String[] args) throws Exception {
        List<Document> secretCodesAndStatus = db2.getAllHashAndVotedStatus();

        for (Document doc : secretCodesAndStatus) {
            if (!doc.getString("hash_id").equals(""))
                System.out.println("Hash ID: " + doc.getString("hash_id") + ", Voted Status: " + doc.getBoolean("voted_status"));
        }
        System.out.println("True if the blockchain has not been tampered with! -"+Blockchain.isChainValid());



    }
}
