package com.openclassrooms.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.dto.AddMessageDto;
import com.openclassrooms.api.exception.ErrorResponse;
import com.openclassrooms.api.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Message endpoints", description = "All operations that can be performed with messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Operation(
		summary = "Add message",
		description = "Requires Bearer JWT. Adds message to the data base.",
        security = @SecurityRequirement(name = "bearerAuth") 
	)
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
				    		schema = @Schema(implementation = AddMessageDto.class)
				    )
		    )
			@RequestBody AddMessageDto data
		) {
		
		// send message data to the service
		messageService.addMessage(data);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Message sent with success");
	    
		return ResponseEntity.ok(response);
	}
}
