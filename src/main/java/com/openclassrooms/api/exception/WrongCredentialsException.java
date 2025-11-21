package com.openclassrooms.api.exception;

@SuppressWarnings("serial")
public class WrongCredentialsException extends RuntimeException {
	public WrongCredentialsException(String message) {
        super(message);
    }
}