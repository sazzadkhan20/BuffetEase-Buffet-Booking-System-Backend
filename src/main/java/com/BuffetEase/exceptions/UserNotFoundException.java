package com.BuffetEase.exceptions;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * Exception when user not found in database
 *
 * WHEN THROWN:
 * - Login with non-existent email
 * - User deleted but token still valid
 * - Invalid user ID in request
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}