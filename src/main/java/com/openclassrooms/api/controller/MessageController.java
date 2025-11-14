package com.openclassrooms.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.model.Message;
import com.openclassrooms.api.service.MessageService;

@RestController
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@PostMapping("/messages")
	public ResponseEntity<Object> addMessage(@RequestBody Message data) {
		System.out.println(data);
		// check if all required data is present in the request		
		if (data.getMessage() == null || data.getMessage() == "" 
			|| data.getUser_id() == null || data.getUser_id() == 0 
			|| data.getRental_id() == null || data.getRental_id() == 0) {
			throw new RuntimeException("You should fill all required data!");
		}
		
		// send message data to the service
		messageService.addMessage(data);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Message sent with success");
	    
		return ResponseEntity.ok(response);
	}
}
