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

interface Block {
    String getHash();
    String getPreviousHash();
    String getData();
}
public class VoteBlock implements Block, Serializable {
    private static final long serialVersionUID = 1L;
    String hash;
    String previousHash;
    String encryptedVote;
    String userHashId;
    String signature;

    public VoteBlock(String previousHash, String encryptedVote, String userHashId, String signature) {
        this.previousHash = previousHash;
        this.encryptedVote = encryptedVote;
        this.userHashId = userHashId;
        this.signature = signature;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String dataToHash = previousHash + encryptedVote + userHashId + signature;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dataToHash.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getHash() {
        return hash;
    }

    @Override
    public String getPreviousHash() {
        return previousHash;
    }

    @Override
    public String getData() {
        return "Vote: " + encryptedVote + ", User: " + userHashId + ", Signature: " + signature;
    }

    public String getVote(){
        return encryptedVote;
    }

    public String getHashID(){
        return userHashId;
    }
    public String getSignature(){
        return signature;
    }

}