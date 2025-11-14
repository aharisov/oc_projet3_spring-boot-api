package com.openclassrooms.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.exception.ErrorResponse;
import com.openclassrooms.api.model.Message;
import com.openclassrooms.api.model.User;
import com.openclassrooms.api.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Message endpoints", description = "All operations that can be performed with messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Operation(summary = "Add message")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Message added", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									example = "{\"message\": \"Message sent with success\"}"
							)
					)
			),
			@ApiResponse(
					responseCode = "400", 
					description = "Something went wrong", 
					content = { 
							@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
					}
			),
			@ApiResponse(responseCode = "401", description = "Unauthorized user", content = @Content)
	})
	@PostMapping("/messages")
	public ResponseEntity<Object> addMessage(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
				    description = "Message data", 
				    required = true,
				    content = @Content(
				    		mediaType = "application/json",
				    		schema = @Schema(implementation = Message.class)
				    )
		    )
			@RequestBody Message data,
			
			@Parameter(description = "Bearer token", required = true)
			@RequestHeader("Authorization") String rawToken
		) {
		
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
