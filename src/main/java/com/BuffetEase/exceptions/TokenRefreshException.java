package com.BuffetEase.exceptions;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * Exception for refresh token errors
 *
 * WHEN THROWN:
 * - Refresh token not found in database
 * - Refresh token expired
 * - Refresh token invalid
 *
 * EXTENDS RuntimeException:
 * - Unchecked exception (don't need to declare in method signature)
 * - Can be caught by global exception handler
 */
public class TokenRefreshException extends RuntimeException {

    /**
     * CONSTRUCTOR WITH MESSAGE
     *
     * USAGE:
     * throw new TokenRefreshException("Refresh token expired");
     */
    public TokenRefreshException(String message) {
        super(message);
    }

    /**
     * CONSTRUCTOR WITH MESSAGE AND CAUSE
     *
     * USAGE:
     * throw new TokenRefreshException("Token error", cause);
     *
     * PARAMETERS:
     * - message: error description
     * - cause: original exception that caused this
     */
    public TokenRefreshException(String message, Throwable cause) {
        super(message, cause);
    }
}