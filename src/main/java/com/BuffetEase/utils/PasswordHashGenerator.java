package com.BuffetEase.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility to generate BCrypt password hashes
 * Run this to generate hashes for your test data
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generate hash for admin password
        String adminPassword = "1234";
        String adminHash = encoder.encode(adminPassword);
        System.out.println("Admin Password: " + adminPassword);
        System.out.println("Admin Hash: " + adminHash);
        System.out.println();

        // Generate hash for customer password
        String customerPassword = "customer123";
        String customerHash = encoder.encode(customerPassword);
        System.out.println("Customer Password: " + customerPassword);
        System.out.println("Customer Hash: " + customerHash);
        System.out.println();

        // Generate hash for another customer
        String customer2Password = "customer456";
        String customer2Hash = encoder.encode(customer2Password);
        System.out.println("Customer2 Password: " + customer2Password);
        System.out.println("Customer2 Hash: " + customer2Hash);
    }
}