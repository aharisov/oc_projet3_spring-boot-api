package com.openclassrooms.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.model.User;
import com.openclassrooms.api.exception.ErrorResponse;
import com.openclassrooms.api.service.JWTService;
import com.openclassrooms.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
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
	@Autowired
	private JWTService jwtService;
	
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
			@RequestBody User data) {
		// check if all required data is present in the request		
		if (data.getEmail() == null || data.getEmail() == "" 
			|| data.getPassword() == null || data.getPassword() == "" 
			|| data.getName() == null) {
			throw new RuntimeException("You should fill all required data!");
		}
		
		// send user registration data to the service
		userService.registerUser(data);
		
		// if user is registered, return response 200 with empty object
		return ResponseEntity.ok(new HashMap<>());
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
			@RequestBody User data) {
		// check if all required data is present in the request		
		if (data.getEmail() == null || data.getEmail() == "" 
			|| data.getPassword() == null || data.getPassword() == "") {
			throw new RuntimeException("You should fill all required data!");
		}
		
		// send user data to the service and user is found and password is ok, generate token
		String token = "";
		if (userService.authUser(data)) {
			token = jwtService.generateToken(data);
		}
		
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
	public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String rawToken) {
		
		// decode token and get user email		
		Jwt decodedJwt = jwtService.decodeToken(rawToken);
		String email = decodedJwt.getSubject();
		
		// get user's data from the DB according to his email and return data		
		User user = userService.getUserInfo(email);
		
		return ResponseEntity.ok(user);
	}
}
