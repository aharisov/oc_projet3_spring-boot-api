package com.openclassrooms.api.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.api.dto.RentalDto;
import com.openclassrooms.api.exception.ErrorResponse;
import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.model.User;
import com.openclassrooms.api.service.FileService;
import com.openclassrooms.api.service.RentalService;
import com.openclassrooms.api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Rental endpoints", description = "All operations that can be performed with rentals")
public class RentalController {

	@Autowired
	private RentalService rentalService;
	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;
	
	@Operation(summary = "Rental creation")
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
	        @RequestPart(value="description", required = false) String description,
	        
			@RequestHeader("Authorization") String rawToken) throws IOException {
		
		// convert String to required BigDecimal
		BigDecimal surfaceNew = (surface != null && !surface.isEmpty()) ? new BigDecimal(surface) : null;
	    BigDecimal priceNew = (price != null && !price.isEmpty()) ? new BigDecimal(price) : null;
	    
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surfaceNew, priceNew, description);
		Rental rental = rentalService.convertToRental(rentalDto);
		
		rental.setOwnerId(userService.getUserId(rawToken));
		
		// check param and save file, store URL
	    if (picture != null && !picture.isEmpty()) {
	        
	    	String fileUrl = fileService.save(picture);
	        
	        rental.setPicture(fileUrl);
	    }
		
		// save rental to the DB
		rentalService.addRental(rental);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Rental created !");
	    
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Get rentals list")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Rental list received", 
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
							        value = "{ \"rentals\": [{ \"id\": 1, \"name\": \"New appart\", \"surface\": 80.0, \"price\": 2000.0, \"picture\": \"http://server_url/api/uploads/image.jpg\", \"description\": \"Lorem ipsum...\", \"ownerId\": 2, \"createdAt\": 2025-11-13T16:01:16Z, \"updatedAt\": \"2025-11-13T16:01:16Z\" }] }"
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
	public ResponseEntity<Map<String, Iterable<Rental>>> getAllRentals(
			@Parameter(description = "Bearer token", required = true)
			@RequestHeader("Authorization") String rawToken
		) {
		
		Iterable<Rental> rentals = rentalService.getAllRentals();
		
		Map<String, Iterable<Rental>> response = new HashMap<>();
	    response.put("rentals", rentals);
	    
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Get rental")
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200", 
					description = "Rental received", 
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = Rental.class)
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
	public ResponseEntity<Optional<Rental>> getRental(
			@Parameter(description = "Rental id in the database", required = true)
			@PathVariable Integer id,
			
			@Parameter(description = "Bearer token", required = true)
			@RequestHeader("Authorization") String rawToken
		) {
		
		Optional<Rental> rental = rentalService.getRentalById(id);
	    
		return ResponseEntity.ok(rental);
	}
	
	@Operation(summary = "Rental update")
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
			@RequestHeader("Authorization") String rawToken,
			@PathVariable Integer id) throws IOException {
		
		// convert String to required BigDecimal
		BigDecimal surfaceNew = (surface != null && !surface.isEmpty()) ? new BigDecimal(surface) : null;
	    BigDecimal priceNew = (price != null && !price.isEmpty()) ? new BigDecimal(price) : null;
	    
		Optional<Rental> existedRental = rentalService.getRentalById(id);
		
		if (existedRental.get().getOwnerId() != userService.getUserId(rawToken)) {
			throw new RuntimeException("You can't change this rental!");
		}
		
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surfaceNew, priceNew, description);
		Rental rental = rentalService.convertToRental(rentalDto);
		
		// check param and save file, store URL
	    if (picture != null && !picture.isEmpty()) {
	        
	    	String fileUrl = fileService.save(picture);
	        
	        rental.setPicture(fileUrl);
	    }
		
	    // set rental id in order to find it in the DB	    
	    rental.setId(id);
	    // set owner id because can't be null	    
	    rental.setOwnerId(existedRental.get().getOwnerId());
	    
		// update rental info in the DB
		rentalService.updateRental(rental);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Rental updated !");
	    
		return ResponseEntity.ok(response);
	}
}
