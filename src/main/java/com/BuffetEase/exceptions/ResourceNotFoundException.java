package com.BuffetEase.exceptions;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * Generic exception for any resource not found
 *
 * WHEN THROWN:
 * - Buffet not found
 * - Booking not found
 * - Any entity not found by ID
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}