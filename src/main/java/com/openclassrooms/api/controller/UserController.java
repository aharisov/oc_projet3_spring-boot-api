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
import com.openclassrooms.api.service.JWTService;
import com.openclassrooms.api.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JWTService jwtService;
	
	@PostMapping("/auth/register")
	public ResponseEntity<Object> userRegister(@RequestBody User data) {
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
	
	@PostMapping("/auth/login")
	public ResponseEntity<Map<String, String>> userLogin(@RequestBody User data) {
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
	
	@GetMapping("/auth/me")
	public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String rawToken) {
		// delete the word Bearer from token string
		String token = rawToken.replace("Bearer ", "").trim();
		
		// decode token and get user email		
		Jwt decodedJwt = jwtService.decodeToken(token);
		String email = decodedJwt.getSubject();
		
		// get user's data from the DB according to his email and return data		
		User user = userService.getUserInfo(email);
		
		return ResponseEntity.ok(user);
	}
}
