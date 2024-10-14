package com.securvote.admin;

import java.util.Scanner;

public class Admin {
    public static void main(String[] args) throws Exception {
        AdminControlBoard.main(args);
        Scanner scanner = new Scanner(System.in) ;
        System.out.println("Do you want to continue to resutls? ");
        String yn =scanner.nextLine();
        if (yn.equalsIgnoreCase("yes"))
            Verification.main(args);
    }
}
