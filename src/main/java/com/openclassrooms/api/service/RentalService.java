package com.openclassrooms.api.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.api.dto.RentalDto;
import com.openclassrooms.api.exception.WrongCredentialsException;
import com.openclassrooms.api.model.Rental;
import com.openclassrooms.api.repository.RentalRepository;
import com.openclassrooms.api.security.SecurityUtils;

import lombok.Data;

@Data
@Service
public class RentalService {
	
	@Autowired
	private final RentalRepository rentalRepository;
	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;
	
	public void addRental(
			String name, 
			String surface, 
			String price, 
			String description, 
			MultipartFile picture
		) throws IOException {
		// convert String to required BigDecimal
		BigDecimal surfaceNew = (surface != null && !surface.isEmpty()) ? new BigDecimal(surface) : null;
	    BigDecimal priceNew = (price != null && !price.isEmpty()) ? new BigDecimal(price) : null;
	    
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surfaceNew, priceNew, description);
		Rental rental = this.convertToRental(rentalDto);
		
		String ownerEmail = SecurityUtils.getCurrentUserEmail();
		
		rental.setOwner_id(userService.getUserId(ownerEmail));
		
		// check param and save file, store URL
	    if (picture != null && !picture.isEmpty()) {
	        
	    	String fileUrl = fileService.save(picture);
	        
	        rental.setPicture(fileUrl);
	    }
	    
		rentalRepository.save(rental);
	}
	
	public Iterable<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
	
	public Optional<Rental> getRentalById(Integer id) {
		return rentalRepository.findById(id);
	}
	
	public void updateRental(
			Integer id,
			String name, 
			String surface, 
			String price, 
			String description, 
			MultipartFile picture
		) throws IOException {
		// convert String to required BigDecimal
		BigDecimal surfaceNew = (surface != null && !surface.isEmpty()) ? new BigDecimal(surface) : null;
	    BigDecimal priceNew = (price != null && !price.isEmpty()) ? new BigDecimal(price) : null;
	    
	    // check if rental exists in the DB
		Optional<Rental> existedRental = this.getRentalById(id);
		
		// get current user's email		
		String userEmail = SecurityUtils.getCurrentUserEmail();
		
		// if current user is not the owner of this rental		
		if (existedRental.get().getOwner_id() != userService.getUserId(userEmail)) {
			throw new WrongCredentialsException("This is not your rental!");
		}
		
		// create rental object from DTO passing params
		RentalDto rentalDto = new RentalDto(name, surfaceNew, priceNew, description);
		Rental rental = this.convertToRental(rentalDto);
		
		// check param and save file, store URL
	    if (picture != null && !picture.isEmpty()) {
	        
	    	String fileUrl = fileService.save(picture);
	        
	        rental.setPicture(fileUrl);
	    }
		
	    // set rental id in order to find it in the DB	    
	    rental.setId(id);
	    
	    // set owner id because can't be null	    
	    rental.setOwner_id(existedRental.get().getOwner_id());
		
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
