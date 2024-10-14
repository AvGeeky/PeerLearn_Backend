package com.securvote.database;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;

public class keys{
    public static KeyPair keypair;
    public static PublicKey publicKey;
    public static PrivateKey privateKey;
    public static String privateString;
    public static String publicString;

    public static HashMap<String,String> generateStringKey() {
        try {
            keypair = encrypt.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        publicKey = keypair.getPublic();
        publicString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        privateKey = keypair.getPrivate();
        privateString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        HashMap<String, String> keys = new HashMap<>();
        keys.put("Public", publicString);
        keys.put("Private", privateString);
        return keys;
    }

}