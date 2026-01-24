package com.BuffetEase.exceptions;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * Exception when trying to register with existing email
 *
 * WHEN THROWN:
 * - User registration with duplicate email
 * - Email update to existing email
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}