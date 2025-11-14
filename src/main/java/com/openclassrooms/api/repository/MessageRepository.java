package com.openclassrooms.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.api.model.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {

}
