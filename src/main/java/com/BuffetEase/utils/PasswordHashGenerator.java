package com.BuffetEase.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminPassword = "1234";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Admin Password: " + adminPassword);
        System.out.println("Admin Hash: " + adminHash);
        System.out.println();

        String customerPassword = "customer123";
        String customerHash = encoder.encode(customerPassword);
        System.out.println("Customer Password: " + customerPassword);
        System.out.println("Customer Hash: " + customerHash);
        System.out.println();

        String customer2Password = "customer456";
        String customer2Hash = encoder.encode(customer2Password);
        System.out.println("Customer2 Password: " + customer2Password);
        System.out.println("Customer2 Hash: " + customer2Hash);
    }
}