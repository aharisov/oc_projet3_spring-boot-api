package com.openclassrooms.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.api.dto.RentalInfoDto;
import com.openclassrooms.api.exception.ErrorResponse;
import com.openclassrooms.api.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rental endpoints", description = "All operations that can be performed with rentals")
public class RentalController {

	@Autowired
	private RentalService rentalService;
	
	@Operation(
		summary = "Rental creation",
		description = "Requires Bearer JWT. Adds one rental to the data base.",
        security = @SecurityRequirement(name = "bearerAuth") 
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Rental created", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									example = "{\"message\": \"Rental created !\"}"
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
	@PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> addRental(
			@Parameter(description = "Rental name", required = false)
			@RequestPart(value="name", required = false) String name,
			
			@Parameter(description = "Rental surface", required = false)
			@RequestPart(value="surface", required = false) String surface, // changed to string because form data can't be BigDecimal and causes error
			
			@Parameter(description = "Rental price", required = false)
			@RequestPart(value="price", required = false) String price, // changed to string because form data can't be BigDecimal and causes error
			
			@Parameter(description = "Rental picture", required = false, content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
	        @RequestPart(value="picture", required = false) MultipartFile picture,
	        
	        @Parameter(description = "Rental description", required = false)
	        @RequestPart(value="description", required = false) String description
        ) throws IOException {
				
		// pass rental data
		rentalService.addRental(name, surface, price, picture, description);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Rental created !");
	    
		return ResponseEntity.ok(response);
	}
	
	@Operation(
        summary = "Get rentals list",
        description = "Requires Bearer JWT. Returns list of rentals.",
        security = @SecurityRequirement(name = "bearerAuth") 
    )
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Rental list received", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = RentalInfoDto.class),
							examples = @ExampleObject(
							        value = "{ \"rentals\": [{ \"id\": 1, \"name\": \"New appart\", \"surface\": 80, \"price\": 2000, \"picture\": \"http://server_url/api/uploads/image.jpg\", \"description\": \"Lorem ipsum...\", \"ownerId\": 2, \"createdAt\": \"2025/11/13\", \"updatedAt\": \"2025/11/13\" }] }"
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
	@GetMapping("/rentals")
	public ResponseEntity<Map<String, List<RentalInfoDto>>> getAllRentals() {
		
		List<RentalInfoDto> rentals = rentalService.getAllRentals();
		
		Map<String, List<RentalInfoDto>> response = new HashMap<>();
	    response.put("rentals", rentals);
	    
		return ResponseEntity.ok(response);
	}
	
	@Operation(
		summary = "Get rental",
		description = "Requires Bearer JWT. Returns one rental.",
        security = @SecurityRequirement(name = "bearerAuth") 
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Rental received", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = RentalInfoDto.class),
							examples = @ExampleObject(
							        value = "{ \"id\": 1, \"name\": \"New appart\", \"surface\": 80, \"price\": 2000, \"picture\": \"http://server_url/api/uploads/image.jpg\", \"description\": \"Lorem ipsum...\", \"ownerId\": 2, \"createdAt\": \"2025/11/13\", \"updatedAt\": \"2025/11/13\" }"
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
	@GetMapping("/rentals/{id}")
	public ResponseEntity<RentalInfoDto> getRental(
			@Parameter(description = "Rental id in the database", required = true)
			@PathVariable Integer id
		) {
		
		RentalInfoDto rental = rentalService.getRentalById(id);
	    
		return ResponseEntity.ok(rental);
	}
	
	@Operation(
		summary = "Rental update",
		description = "Requires Bearer JWT. Updates rental's info.",
        security = @SecurityRequirement(name = "bearerAuth") 
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Rental updated", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(
									example = "{\"message\": \"Rental updated !\"}"
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
	@PutMapping(value = "/rentals/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Map<String, String>> updateRental(
			@Parameter(description = "Rental name", required = false)
			@RequestPart(value="name", required = false) String name,
			
			@Parameter(description = "Rental surface", required = false)
			@RequestPart(value="surface", required = false) String surface, // changed to string because form data can't be BigDecimal and causes error
			
			@Parameter(description = "Rental price", required = false)
			@RequestPart(value="price", required = false) String price, // changed to string because form data can't be BigDecimal and causes error
			
			@Parameter(description = "Rental picture", required = false, content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
	        @RequestPart(value="picture", required = false) MultipartFile picture,
	        
	        @Parameter(description = "Rental description", required = false)
	        @RequestPart(value="description", required = false) String description,
			
	        @Parameter(description = "Rental id in the database", required = true)
			@PathVariable Integer id
		) throws IOException {
	    
		// update rental info in the DB
		rentalService.updateRental(id, name, surface, price, picture, description);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Rental updated !");
	    
		return ResponseEntity.ok(response);
	}
}
