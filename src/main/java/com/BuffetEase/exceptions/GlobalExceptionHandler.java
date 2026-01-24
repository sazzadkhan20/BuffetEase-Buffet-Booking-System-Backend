package com.BuffetEase.exceptions;

import com.BuffetEase.dtos.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * ========================================
 * WHAT IS THIS?
 * ========================================
 *
 * GLOBAL EXCEPTION HANDLER
 *
 * Catches ALL exceptions thrown in the application
 * Returns standardized error responses
 *
 * @RestControllerAdvice:
 * - Applies to ALL controllers
 * - Returns JSON automatically
 * - Catches exceptions globally
 *
 * HOW IT WORKS:
 *
 * 1. Exception thrown anywhere in app
 *    ↓
 * 2. Spring looks for @ExceptionHandler method
 *    ↓
 * 3. Calls matching handler method
 *    ↓
 * 4. Returns ErrorResponse as JSON
 *    ↓
 * 5. Client receives standardized error
 *
 * BENEFITS:
 * - No try-catch in controllers
 * - Consistent error format
 * - Single place to handle errors
 * - Easy to maintain
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========================================
    // 1. TOKEN REFRESH EXCEPTIONS (403)
    // ========================================

    /**
     * HANDLE REFRESH TOKEN ERRORS
     *
     * WHEN TRIGGERED:
     * - Refresh token not found
     * - Refresh token expired
     *
     * @ExceptionHandler(TokenRefreshException.class)
     * - Catches TokenRefreshException
     * - Anywhere in the application
     *
     * PARAMETERS:
     * - ex: the exception object
     * - request: HTTP request where error occurred
     *
     * RETURNS:
     * - ResponseEntity with ErrorResponse
     * - Status: 403 FORBIDDEN
     *
     * WHY 403?
     * - User was authenticated (has refresh token)
     * - But token is no longer valid
     * - Must re-authenticate
     */
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenRefreshException(
            TokenRefreshException ex,
            HttpServletRequest request
    ) {
        /**
         * CREATE ERROR RESPONSE
         *
         * Status: 403
         * Error: "Forbidden"
         * Message: From exception (e.g., "Refresh token expired")
         * Path: Request URI (e.g., "/api/auth/refresh")
         */
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        /**
         * RETURN RESPONSE
         *
         * ResponseEntity.status(HttpStatus.FORBIDDEN)
         * - Sets HTTP status to 403
         *
         * .body(errorResponse)
         * - Sets response body
         * - Spring converts to JSON
         */
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    // ========================================
    // 2. AUTHENTICATION EXCEPTIONS (401)
    // ========================================

    /**
     * HANDLE AUTHENTICATION ERRORS
     *
     * WHEN TRIGGERED:
     * - Invalid credentials during login
     * - Authentication failed
     *
     * WHY 401?
     * - User provided credentials
     * - But they're wrong
     * - Not authenticated
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Authentication failed: " + ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    /**
     * HANDLE BAD CREDENTIALS
     *
     * WHEN TRIGGERED:
     * - Wrong password during login
     *
     * SPECIFIC CASE OF AuthenticationException
     * More specific handler = takes precedence
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid email or password",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    /**
     * HANDLE USER NOT FOUND
     *
     * WHEN TRIGGERED:
     * - Login with non-existent email
     * - UserDetailsService can't find user
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid email or password", // Don't reveal user doesn't exist
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    // ========================================
    // 3. JWT TOKEN EXCEPTIONS (401)
    // ========================================

    /**
     * HANDLE EXPIRED JWT TOKEN
     *
     * WHEN TRIGGERED:
     * - JWT token expired
     * - Not caught by auto-refresh
     *
     * NOTE: Auto-refresh usually handles this
     * This is a fallback
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredJwtException(
            ExpiredJwtException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "JWT token expired. Please refresh your token.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    /**
     * HANDLE MALFORMED JWT TOKEN
     *
     * WHEN TRIGGERED:
     * - Token format invalid
     * - Not a valid JWT
     * - Missing parts
     *
     * EXAMPLE:
     * - "notavalidtoken"
     * - "eyJhbGc" (incomplete)
     */
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJwtException(
            MalformedJwtException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid JWT token format",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    /**
     * HANDLE JWT SIGNATURE EXCEPTION
     *
     * WHEN TRIGGERED:
     * - Token signature doesn't match
     * - Token tampered with
     * - Wrong secret key
     *
     * SECURITY CRITICAL!
     * Someone tried to forge a token
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponseDTO> handleSignatureException(
            SignatureException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid JWT signature",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    /**
     * HANDLE CUSTOM INVALID TOKEN EXCEPTION
     *
     * WHEN TRIGGERED:
     * - Our custom InvalidTokenException
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTokenException(
            InvalidTokenException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    // ========================================
    // 4. ACCESS DENIED EXCEPTION (403)
    // ========================================

    /**
     * HANDLE ACCESS DENIED
     *
     * WHEN TRIGGERED:
     * - User authenticated (valid token)
     * - But doesn't have required role
     * - Example: CUSTOMER accessing /api/admin/**
     *
     * WHY 403 (not 401)?
     * - We know WHO the user is (authenticated)
     * - But they can't access this resource
     * - Authorization failed, not authentication
     *
     * EXAMPLE SCENARIO:
     * - Customer user: "john@example.com"
     * - Has valid token
     * - Tries: GET /api/admin/dashboard
     * - Has role: ROLE_CUSTOMER
     * - Needs role: ROLE_ADMIN
     * - Result: 403 Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "You don't have permission to access this resource",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    // ========================================
    // 5. RESOURCE NOT FOUND (404)
    // ========================================

    /**
     * HANDLE RESOURCE NOT FOUND
     *
     * WHEN TRIGGERED:
     * - User not found by ID
     * - Buffet not found
     * - Booking not found
     * - Any entity not found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * HANDLE USER NOT FOUND (404)
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    // ========================================
    // 6. VALIDATION EXCEPTIONS (400)
    // ========================================

    /**
     * HANDLE VALIDATION ERRORS
     *
     * WHEN TRIGGERED:
     * - @Valid annotation on request body
     * - Validation constraints failed
     *
     * EXAMPLE:
     * public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request)
     *
     * RegisterRequest has:
     * @NotBlank(message = "Email is required")
     * private String email;
     *
     * If email is blank → MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        /**
         * COLLECT ALL VALIDATION ERRORS
         *
         * ex.getBindingResult() has all validation failures
         * .getFieldErrors() gets field-specific errors
         */
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            /**
             * error.getField() = field name (e.g., "email")
             * error.getDefaultMessage() = error message (e.g., "Email is required")
             *
             * Combine: "email: Email is required"
             */
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        /**
         * CREATE ERROR RESPONSE WITH VALIDATION ERRORS
         */
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * HANDLE EMAIL ALREADY EXISTS
     *
     * WHEN TRIGGERED:
     * - Registration with duplicate email
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    // ========================================
    // 7. GENERIC EXCEPTIONS (500)
    // ========================================

    /**
     * HANDLE ALL OTHER EXCEPTIONS
     *
     * WHEN TRIGGERED:
     * - Any exception not handled above
     * - Unexpected errors
     * - Database errors
     * - NPE, etc.
     *
     * THIS IS THE FALLBACK
     *
     * IMPORTANT:
     * - Log the full exception for debugging
     * - Don't expose internal details to client
     * - Return generic message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        /**
         * LOG THE FULL ERROR
         *
         * For debugging purposes
         * Don't send to client (security risk)
         */
        System.err.println("Unexpected error: " + ex.getClass().getName());
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();

        /**
         * SEND GENERIC MESSAGE TO CLIENT
         *
         * Don't reveal internal errors
         * Could expose security vulnerabilities
         */
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}