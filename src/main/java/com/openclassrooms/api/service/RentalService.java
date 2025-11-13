package com.openclassrooms.api.service;

import java.util.Optional;

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
	
	public void addRental(Rental rental) {
		rentalRepository.save(rental);
	}
	
	public Iterable<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
	
	public Optional<Rental> getRentalById(Integer id) {
		return rentalRepository.findById(id);
	}
}
