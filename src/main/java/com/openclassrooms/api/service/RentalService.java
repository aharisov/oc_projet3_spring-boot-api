package com.openclassrooms.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {
	
	@Autowired
	private final RentalRepository rentalRepository;
	
	public Iterable<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
}
