package com.openclassrooms.api.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.api.dto.CreateRentalDto;
import com.openclassrooms.api.dto.RentalInfoDto;
import com.openclassrooms.api.dto.UpdateRentalDto;
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
	
	@Autowired
    private ModelMapper mapper;
	
	public void addRental(
			String name, 
			String surface, 
			String price, 
			MultipartFile picture,
			String description
		) throws IOException {
	    
		// check picture field, save file and get it's url
		String pictureUrl = "";
		
	    if (picture != null && !picture.isEmpty()) {
	        pictureUrl = fileService.save(picture);
	    }
	    
	    // get owner id	    
	    String ownerEmail = SecurityUtils.getCurrentUserEmail();
	    Integer ownerId = userService.getUserId(ownerEmail);
	    
		// create DTO object
		CreateRentalDto rentalDto = new CreateRentalDto(name, surface, price, pictureUrl, description, ownerId);
		
		// convert DTO to the Rental entity		
		Rental rentalInfo = mapper.map(rentalDto, Rental.class);
		
		rentalRepository.save(rentalInfo);
	}
	
	public List<RentalInfoDto> getAllRentals() {
		List<Rental> rentals = (List<Rental>) rentalRepository.findAll();
		
		// convert entity to the DTO	
		 return rentals.stream()
		            .map(rental -> mapper.map(rental, RentalInfoDto.class))
		            .toList();
	}
	
	public RentalInfoDto getRentalById(Integer id) {
		Optional<Rental> rental = rentalRepository.findById(id);
		
		// convert entity to the DTO	
		RentalInfoDto rentalInfo = mapper.map(rental, RentalInfoDto.class);
		
		return rentalInfo;
	}
	
	public void updateRental(
			Integer id,
			String name, 
			String surface, 
			String price, 
			MultipartFile picture,
			String description
		) throws IOException {
		
		// check if rental exists in the DB
		Optional<Rental> existedRental = rentalRepository.findById(id);
		
		// get current user's email		
		String userEmail = SecurityUtils.getCurrentUserEmail();
		
		// if current user is not the owner of this rental		
		if (existedRental.get().getOwner_id() != userService.getUserId(userEmail)) {
			throw new WrongCredentialsException("This is not your rental!");
		}
		
		// check picture field, save file and get it's url
		String pictureUrl = "";
		
	    if (picture != null && !picture.isEmpty()) {
	        pictureUrl = fileService.save(picture);
	    }
	    
	    // get owner id	    
	    Integer ownerId = userService.getUserId(userEmail);
	    
		// create DTO object
		UpdateRentalDto rentalDto = new UpdateRentalDto(id, name, surface, price, pictureUrl, description, ownerId);
		
		// convert DTO to the Rental entity		
		Rental rentalInfo = mapper.map(rentalDto, Rental.class);
		
	    if (existedRental != null) {
			rentalRepository.save(rentalInfo);
		}
	}
}
