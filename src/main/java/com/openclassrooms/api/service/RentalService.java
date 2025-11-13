package com.openclassrooms.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.dto.RentalDto;
import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {
	
	@Autowired
	private final RentalRepository rentalRepository;
	@Autowired
	private FileService fileService;
	
	public void addRental(Rental rental) {
		rentalRepository.save(rental);
	}
	
	public Iterable<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
	
	public Optional<Rental> getRentalById(Integer id) {
		return rentalRepository.findById(id);
	}
	
	public void updateRental(Rental rental) {
		if (rentalRepository.findById(rental.getId()) != null) {
			rentalRepository.save(rental);
		}
	}
	
	public Rental convertToRental(RentalDto rentalDto) {
		// create new instance of rental object
		Rental rental = new Rental();
		
		// add data to the object
		rental.setName(rentalDto.getName());
		rental.setSurface(rentalDto.getSurface());
		rental.setPrice(rentalDto.getPrice());
		rental.setDescription(rentalDto.getDescription());
		
		return rental;
	}
}
