package com.BuffetEase.exceptions;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * Exception for invalid JWT tokens
 *
 * WHEN THROWN:
 * - Token signature invalid
 * - Token malformed
 * - Token expired (can also use ExpiredJwtException)
 * - Token tampered with
 *
 * WHY CUSTOM EXCEPTION?
 * - Better error messages
 * - Centralized handling
 * - Easier to catch specific errors
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}