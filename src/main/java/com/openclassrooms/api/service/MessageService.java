package com.openclassrooms.api.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.api.dto.AddMessageDto;
import com.openclassrooms.api.exception.NullFieldException;
import com.openclassrooms.api.model.Message;
import com.openclassrooms.api.repository.MessageRepository;

import lombok.Data;

@Data
@Service
public class MessageService {
	
	@Autowired
	private final MessageRepository messageRepository;
	
	@Autowired
    private ModelMapper mapper;
	
	public void addMessage(AddMessageDto message) {
		// check if all required data is present in the request		
		if (message.getMessage() == null || message.getUser_id() == null || message.getRental_id() == null) {
			throw new NullFieldException("You should fill all required data!");
		}
		
		// convert DTO to the Message entity		
		Message messageInfo = mapper.map(message, Message.class);
		
		messageRepository.save(messageInfo);
	}
}
