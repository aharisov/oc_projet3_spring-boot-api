package com.openclassrooms.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.model.User;
import com.openclassrooms.api.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {
	
	@Autowired
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public void registerUser(User user) {
		// verify if the user with the sent email exists 
		// throw exception to avoid registration with the same email
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new RuntimeException("User with this email already exists!");
        }
		
		// encode password in order to stock it in the DB
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		// save user data to the DB
        userRepository.save(user);
    }
	
	public Boolean authUser(User user) {
		// search for the user in the DB 		
		User existedUser = userRepository.findByEmail(user.getEmail());
		
		// if user with such email not found or password is incorrect	
		// we use common formulation in order to confuse potential hacker		
		if (existedUser == null || !passwordEncoder.matches(user.getPassword(), existedUser.getPassword())) {
			throw new RuntimeException("User email or password is incorrect!");
        }
		
		// if user found and password is ok		
		return true;
	}
}
