package com.openclassrooms.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.api.model.Rental;

@Repository
public interface RentalRepository extends CrudRepository<Rental, Integer> {

}
