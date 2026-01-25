package com.BuffetEase.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @NotEmpty
    private int status;

    @NotEmpty
    private String error;

    @NotEmpty
    private String message;

    @NotEmpty
    private String path;

    private List<String> errors;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponseDTO(int status, String error, String message, String path, List<String> errors) {
        this(status, error, message, path);
        this.errors = errors;
    }

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