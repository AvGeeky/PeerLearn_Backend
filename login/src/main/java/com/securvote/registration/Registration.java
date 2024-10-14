package com.securvote.registration;
import com.securvote.database.*;
import com.securvote.login.Login;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;




public class Registration {
    public static void main(String[] args) throws Exception {;
        {
            Scanner scanner = new Scanner(System.in);
            String secretcode = Login.secretid;

            if (db1.findUser(secretcode)) {
                HashMap<String, String> users = db2.getUserDetails(secretcode);

                System.out.print("Please enter your email: "); //INPUT EMAIL
                String email = scanner.nextLine();

                if (!email.equals(users.get("email"))){
                    System.out.println("Error! Authentication failed. "); //ERROR
                    throw new QPE("Error! Authentication failed.");
                }

                System.out.println("You have been verified. Set username and password.");

                System.out.print("Please enter your new username: "); //INPUT USERNAME
                String username = scanner.nextLine();

                System.out.print("Please enter your new password: "); //INPUT PASSWORD
                String password = scanner.nextLine();

                String a = db1.updateUsername(secretcode, username);
                String b = db1.updatePassword(secretcode, password);

                if (a.equals("Success") && b.equals("Success"))
                    System.out.println("Your Username & Password has been Set."); //PRINTS CONFIRMATION
                else System.out.println("Error! ");


                users.put("username", username);
                users.put("password", password);
                db1.updatePassword(secretcode,password);
                db1.updateUsername(secretcode,username);
                db2.setUserDetails(secretcode, users);
                System.out.println("User Details:");
                for (String key : users.keySet()) {
                    System.out.printf("%s: %s%n", key, users.get(key)); //PRINTS USER DETAILS
                }
                users.put("username", username);
                users.put("password", password);
                db2.setUserDetails(secretcode, users);

                keys.generateStringKey();
                PublicKey pubk = keys.publicKey;
                PrivateKey prik = keys.privateKey;

                String hash = encrypt.generateHash(keys.publicString, "sp2005"); //PASSWORD TO BE PUT IN ENV FILE

                db2.storePublicKey(secretcode, pubk);
                db2.storeHashID(secretcode,hash);

                System.out.println("Your Hash ID is: "+hash+". Please store it for future use."); //DISPLAYS HASH ID TO USER
                filemanager.writeToFile(secretcode,prik);

                System.out.println("Registration Over!");



            }
            else throw new QPE("Error! Authentication failed."); //OUTPUT TEXT IF REGISTRATION DONE MORE THAN ONCE/ERROR

            }

        }
    }



