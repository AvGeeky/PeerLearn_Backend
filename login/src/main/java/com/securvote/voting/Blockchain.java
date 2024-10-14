package com.securvote.voting;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.io.Serializable;


public class Blockchain {
    public static List<VoteBlock> chain;

    static {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    public static VoteBlock createGenesisBlock() {
        return new VoteBlock("0", "Genesis Vote", "GenesisUser", "GenesisSignature");
    }

    public static void addBlock(String encryptedVote, String userHashId, String signature) {
        VoteBlock previousBlock = chain.get(chain.size() - 1);
        VoteBlock newBlock = new VoteBlock(previousBlock.getHash(), encryptedVote, userHashId, signature);
        chain.add(newBlock);
    }

    public static boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            VoteBlock currentBlock = chain.get(i);
            VoteBlock previousBlock = chain.get(i - 1);


            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hashes not equal");
                return false;
            }


            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hashes not equal");
                return false;
            }
        }
        return true;
    }


    public static void printBlockchain() {
        for (VoteBlock block : chain) {
            System.out.println(block.getData());
        }
    }


    public static void saveBlockchainToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(chain);
            System.out.println("Blockchain saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadBlockchainFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            chain = (List<VoteBlock>) ois.readObject();
            System.out.println("Blockchain loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void deleteBlockchainFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.delete()) {
            System.out.println("Blockchain file deleted.");
        } else {
            System.out.println("Failed to delete blockchain file.");
        }
    }

}


