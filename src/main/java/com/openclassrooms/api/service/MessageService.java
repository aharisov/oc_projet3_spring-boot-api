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
		messageRepository.save(message);
	}
}
