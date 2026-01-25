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

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenRefreshException(
            TokenRefreshException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

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

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(
            InvalidCredentialsException ex,
            HttpServletRequest request
    ) {
        System.out.println("InvalidCredentialsException caught: " + ex.getMessage());

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        System.err.println("Unexpected error: " + ex.getClass().getName());
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
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