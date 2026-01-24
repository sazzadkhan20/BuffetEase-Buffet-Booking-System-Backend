package com.BuffetEase.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * Standardized error response format
 *
 * WHY?
 * - Consistent error structure across all endpoints
 * - Client knows what to expect
 * - Easy to parse and display
 *
 * EXAMPLE RESPONSE:
 * {
 *   "timestamp": "2024-01-15T10:30:00",
 *   "status": 401,
 *   "error": "Unauthorized",
 *   "message": "Invalid JWT token",
 *   "path": "/api/admin/dashboard"
 * }
 */
public class ErrorResponseDTO {

    /**
     * WHEN THE ERROR OCCURRED
     *
     * @JsonFormat: formats as ISO 8601 date-time
     * Example: "2024-01-15T10:30:00"
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * HTTP STATUS CODE
     *
     * Examples:
     * - 400: Bad Request
     * - 401: Unauthorized
     * - 403: Forbidden
     * - 404: Not Found
     * - 500: Internal Server Error
     */
    private int status;

    /**
     * HTTP STATUS TEXT
     *
     * Examples:
     * - "Bad Request"
     * - "Unauthorized"
     * - "Forbidden"
     * - "Not Found"
     */
    private String error;

    /**
     * DETAILED ERROR MESSAGE
     *
     * Human-readable description of what went wrong
     *
     * Examples:
     * - "Invalid JWT token"
     * - "User not found with email: admin@example.com"
     * - "Refresh token expired"
     */
    private String message;

    /**
     * REQUEST PATH WHERE ERROR OCCURRED
     *
     * Examples:
     * - "/api/auth/login"
     * - "/api/admin/dashboard"
     * - "/api/user/profile"
     */
    private String path;

    /**
     * VALIDATION ERRORS (OPTIONAL)
     *
     * Used for validation failures
     *
     * Example:
     * ["Email is required", "Password must be at least 4 characters"]
     */
    private List<String> errors;

    // ========================================
    // CONSTRUCTORS
    // ========================================

    /**
     * DEFAULT CONSTRUCTOR
     */
    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * MAIN CONSTRUCTOR
     *
     * USAGE:
     * new ErrorResponse(401, "Unauthorized", "Invalid token", "/api/admin/dashboard")
     */
    public ErrorResponseDTO(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * CONSTRUCTOR WITH VALIDATION ERRORS
     *
     * USAGE:
     * new ErrorResponse(400, "Bad Request", "Validation failed", "/api/auth/register", errors)
     */
    public ErrorResponseDTO(int status, String error, String message, String path, List<String> errors) {
        this(status, error, message, path);
        this.errors = errors;
    }

    // ========================================
    // GETTERS AND SETTERS
    // ========================================

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}