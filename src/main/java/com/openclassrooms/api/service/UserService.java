package com.openclassrooms.api.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.dto.UserInfoDto;
import com.openclassrooms.api.dto.UserLoginDto;
import com.openclassrooms.api.dto.UserRegisterDto;
import com.openclassrooms.api.exception.NullFieldException;
import com.openclassrooms.api.exception.UserExistsException;
import com.openclassrooms.api.exception.WrongCredentialsException;
import com.openclassrooms.api.model.User;
import com.openclassrooms.api.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	// library for the simple conversion of entities into DTOs
    private ModelMapper mapper;
	
	private final PasswordEncoder passwordEncoder;
	private final JWTService jwtService;
	
	public void registerUser(UserRegisterDto user) {
		// check if all required data is present in the request		
		if (user.getEmail() == null || user.getPassword() == null || user.getName() == null) {
			throw new NullFieldException("You should fill all required data!");
		}
				
		// verify if the user with the sent email exists 
		// throw exception to avoid registration with the same email
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new UserExistsException("User with this email already exists!");
        }
		
		// encode password in order to stock it in the DB
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		// convert user DTO to entity	
		User userInfo = mapper.map(user, User.class);
		
		// save user data to the DB
        userRepository.save(userInfo);
    }
	
	public String authUser(UserLoginDto user) {
		// search for the user in the DB 		
		User existedUser = userRepository.findByEmail(user.getEmail());
		
		// if user with such email not found or password is incorrect	
		// we use common formulation in order to confuse potential hacker		
		if (existedUser == null || !passwordEncoder.matches(user.getPassword(), existedUser.getPassword())) {
			throw new WrongCredentialsException("User email or password is incorrect!");
        }
		
		String token = jwtService.generateToken(existedUser.getEmail());
		
		return token;
	}
	
	public UserInfoDto getUserInfo(String email) {
		// search for user with this email	
		User user = userRepository.findByEmail(email);
		
		// convert user entity data to DTO	
		UserInfoDto userInfo = mapper.map(user, UserInfoDto.class);
		
		return userInfo;
	}
	
	public Integer getUserId(String email) {
		// get user's data from the DB according to his email in order to know his id	
		User user = userRepository.findByEmail(email);
		
		return user.getId();
	}
}
