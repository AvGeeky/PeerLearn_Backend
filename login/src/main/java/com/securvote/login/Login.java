package com.securvote.login;
import com.securvote.database.*;
import com.securvote.registration.QPE;
import com.securvote.registration.Registration;


import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
public class Login {
    public static PrivateKey user_prik;
    public static PublicKey admin_pubk;
    public static boolean authorised=false;
    public static String secretid;
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your secret-id: "); //INPUT secret id
        secretid = scanner.nextLine();
        if (db1.getUser(secretid).get("username").equalsIgnoreCase("") || db1.getUser(secretid).get("username").equalsIgnoreCase("username")){
            Registration.main(args);
        } //CALLS REGN CLASS IF USERNAME IS EMPTY OR UNFILLED.
        HashMap<String, String > hsh = db1.getUser(secretid);

        if (hsh==null)
            throw new QPE("SECRETID NOT FOUND!"); //output error



        System.out.print("Please enter your username: "); //INPUT USERNAME
        String username = scanner.nextLine();

        System.out.print("Please enter your password: "); //INPUT PASSWORD
        String password = scanner.nextLine();
        boolean hasVoted = db2.getVotedStatus(secretid);

        if (hasVoted)
            throw new QPE("You have already voted!.");

        if (!hsh.get("username").equals(username) || !hsh.get("password").equals(password))
            throw new QPE("Wrong Credentials!");

        user_prik = filemanager.retrievedContent(secretid);
        admin_pubk = db2.getPublicKey("ADMIN");
        authorised=true;
//        System.out.println(authorised);
//        System.out.println(user_prik);
//        System.out.println(admin_pubk);

    }
}
