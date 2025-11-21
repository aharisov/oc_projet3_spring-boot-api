package com.openclassrooms.api.exception;

@SuppressWarnings("serial")
public class NullFieldException extends RuntimeException {
    public NullFieldException(String message) {
        super(message);
    }
}