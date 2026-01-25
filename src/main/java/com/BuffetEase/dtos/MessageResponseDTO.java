package com.BuffetEase.dtos;

import jakarta.validation.constraints.NotEmpty;

public class MessageResponseDTO {

    @NotEmpty
    private String message;

    public MessageResponseDTO() {}

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}