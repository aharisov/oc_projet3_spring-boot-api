package com.openclassrooms.api.dto;

/**
 * DTO for user login
 */
public class UserLoginDto {
	private String email;
	private String password;
    
    // REQUIRED for ModelMapper
    public UserLoginDto() {}

    // Constructor
    public UserLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
