package com.openclassrooms.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.service.RentalService;

@RestController
public class RentalController {

	@Autowired
	private RentalService rentalService;
	
	@GetMapping("/rentals")
	public ResponseEntity<Map<String, Iterable<Rental>>> getAllRentals() {
		
		Iterable<Rental> rentals = rentalService.getAllRentals();
		
		Map<String, Iterable<Rental>> response = new HashMap<>();
	    response.put("rentals", rentals);
	    
		return ResponseEntity.ok(response);
	}
}
