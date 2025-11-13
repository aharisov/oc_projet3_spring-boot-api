package com.openclassrooms.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
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
	private final JWTService jwtService;
	
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
	
	public User getUserInfo(String email) {
		// search for user with this email and simply return his data		
		return userRepository.findByEmail(email);
	}
	
	public Integer getUserId(String rawToken) {
		// decode token and get user email		
		Jwt decodedJwt = jwtService.decodeToken(rawToken);
		String email = decodedJwt.getSubject();
		
		// get user's data from the DB according to his email in order to know his id	
		User user = userRepository.findByEmail(email);
		
		return user.getId();
	}
}
