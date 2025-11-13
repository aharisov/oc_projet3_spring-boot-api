package com.openclassrooms.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	
	// search for user by email
	User findByEmail(String email);
}