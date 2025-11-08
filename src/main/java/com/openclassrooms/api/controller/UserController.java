package com.openclassrooms.api.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.model.User;
import com.openclassrooms.api.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/auth/register")
	public ResponseEntity<Object> userRegister(@RequestBody User data) {
		// send user registration data to the service
		userService.registerUser(data);
		
		// if user is registered, return response 200 with empty object
		return ResponseEntity.ok(new HashMap<>());
	}
}
