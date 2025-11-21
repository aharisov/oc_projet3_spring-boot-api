package com.openclassrooms.api.exception;

@SuppressWarnings("serial")
public class UserExistsException extends RuntimeException {
	public UserExistsException(String message) {
		super(message);
	}
}