package com.openclassrooms.api.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.service.FileService;
import com.openclassrooms.api.service.RentalService;
import com.openclassrooms.api.service.UserService;

@RestController
public class RentalController {

	@Autowired
	private RentalService rentalService;
	@Autowired
	private UserService userService;
	@Autowired
	private FileService fileService;
	
	@PostMapping("/rentals")
	public ResponseEntity<Map<String, String>> addRental(
			@RequestPart("name") String name,
			@RequestParam("surface") BigDecimal surface,
			@RequestParam("price") BigDecimal price,
	        @RequestPart("picture") MultipartFile picture,
	        @RequestPart("description") String description,
			@RequestHeader("Authorization") String rawToken) throws IOException {
		
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surface, price, description);
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
		
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surface, price, description);
		Rental rental = rentalService.convertToRental(rentalDto);
		
		rental.setOwnerId(userService.getUserId(rawToken));
		
		// check param and save file, store URL
	    if (picture != null && !picture.isEmpty()) {
	        
	    	String fileUrl = fileService.save(picture);
	        
	        rental.setPicture(fileUrl);
	    }
		
	    // set rental id in order to find it in the DB	    
	    rental.setId(id);
	    
		// update rental info in the DB
		rentalService.updateRental(rental);
		
		Map<String, String> response = new HashMap<>();
	    response.put("message", "Rental updated !");
	    
		return ResponseEntity.ok(response);
	}
}
