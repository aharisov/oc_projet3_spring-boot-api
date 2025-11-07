package com.openclassrooms.api.exception;

public class ErrorResponse {
	private String status;
    private String message;

    public ErrorResponse(String message) {
        this.status = "error";
        this.message = message;
    }

    // getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
