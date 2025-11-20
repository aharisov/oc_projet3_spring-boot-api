package com.openclassrooms.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.model.Message;
import com.openclassrooms.api.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService {
	
	@Autowired
	private final MessageRepository messageRepository;
	
	public void addMessage(Message message) {
		// check if all required data is present in the request		
		if (message.getMessage() == null || message.getMessage() == "" 
			|| message.getUser_id() == null || message.getUser_id() == 0 
			|| message.getRental_id() == null || message.getRental_id() == 0) {
			throw new RuntimeException("You should fill all required data!");
		}
		messageRepository.save(message);
	}
}
