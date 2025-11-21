package com.openclassrooms.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.model.User;
import com.openclassrooms.api.security.SecurityUtils;
import com.openclassrooms.api.dto.UserInfoDto;
import com.openclassrooms.api.dto.UserLoginDto;
import com.openclassrooms.api.dto.UserRegisterDto;
import com.openclassrooms.api.exception.ErrorResponse;
import com.openclassrooms.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User endpoints", description = "All operations that can be performed with users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Operation(summary = "User registration")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User registered", 
					content = { 
							@Content(mediaType = "application/json") 
					}
			),
			@ApiResponse(
					responseCode = "400", 
					description = "Invalid credentials", 
					content = { 
							@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
					}
			)
	})
	@PostMapping("/auth/register")
	public ResponseEntity<Object> userRegister(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "User register data", 
				    required = true,
				    content = @Content(mediaType = "application/json",
				      schema = @Schema(implementation = User.class),
				      examples = @ExampleObject (
				    		  value = "{ \"name\": \"Alex\", \"email\": \"test@test.com\", \"password\": \"*******\" }")
				    )
		    )
			@RequestBody UserRegisterDto data) {
		
		// send user registration data to the service and receive token
		String token = userService.registerUser(data);
		
		// add token to response and return it
		Map<String, String> response = new HashMap<>();
	    response.put("token", token);
	    
	    return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "User login")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User logged in and is authorized",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
							)
			    )
			),
			@ApiResponse(
					responseCode = "400", 
					description = "Invalid credentials", 
					content = { 
							@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
					}
			)
	})
	@PostMapping("/auth/login")
	public ResponseEntity<Map<String, String>> userLogin(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "User auth data", 
				    required = true,
				    content = @Content(mediaType = "application/json",
				      schema = @Schema(implementation = User.class),
				      examples = @ExampleObject (value = "{ \"email\": \"test@test.com\", \"password\": \"*******\" }")
				    )
		    )
			@RequestBody UserLoginDto data) {
		
		// send user data to the service and receive token
		String token = userService.authUser(data);
		
		// add token to response and return it
		Map<String, String> response = new HashMap<>();
	    response.put("token", token);
	    
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Get user info")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "User info received", 
					content = { 
							@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) 
					}
			),
			@ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content)
	})
	@GetMapping("/auth/me")
	//@Parameter(description = "Bearer token", required = true)
	public ResponseEntity<UserInfoDto> getUserInfo() {
		// get user email 
		String email = SecurityUtils.getCurrentUserEmail();
		
		// get user's data from the DB	
		UserInfoDto userInfo = userService.getUserInfo(email);
		
		return ResponseEntity.ok(userInfo);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserInfoDto> getUserById(
			@PathVariable Integer id,
			
			@RequestHeader("Authorization") String rawToken
		) {
		
		UserInfoDto userInfo = userService.getUserById(id);
	    
		return ResponseEntity.ok(userInfo);
	}
}
