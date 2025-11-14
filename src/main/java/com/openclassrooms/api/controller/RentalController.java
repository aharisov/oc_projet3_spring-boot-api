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
					description = "Invalid credentials", 
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
	
	@GetMapping("/rentals")
	public ResponseEntity<Map<String, Iterable<Rental>>> getAllRentals() {
		
		Iterable<Rental> rentals = rentalService.getAllRentals();
		
		Map<String, Iterable<Rental>> response = new HashMap<>();
	    response.put("rentals", rentals);
	    
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/rentals/{id}")
	public ResponseEntity<Optional<Rental>> getRental(@PathVariable Integer id) {
		
		Optional<Rental> rental = rentalService.getRentalById(id);
	    
		return ResponseEntity.ok(rental);
	}
	
	@PutMapping("/rentals/{id}")
	public ResponseEntity<Map<String, String>> updateRental(
			@RequestPart("name") String name,
			@RequestParam("surface") BigDecimal surface,
			@RequestParam("price") BigDecimal price,
	        @RequestPart("picture") MultipartFile picture,
	        @RequestPart("description") String description,
			@RequestHeader("Authorization") String rawToken,
			@PathVariable Integer id) throws IOException {
		
		Optional<Rental> existedRental = rentalService.getRentalById(id);
		
		if (existedRental.get().getOwnerId() != userService.getUserId(rawToken)) {
			throw new RuntimeException("You can't change this rental!");
		}
		
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surface, price, description);
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
