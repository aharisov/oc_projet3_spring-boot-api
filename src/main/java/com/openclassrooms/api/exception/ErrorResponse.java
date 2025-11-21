package com.openclassrooms.api.exception;

public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    // getters
    public String getMessage() { return message; }
}
