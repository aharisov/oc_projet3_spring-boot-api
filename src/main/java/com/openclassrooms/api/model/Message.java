package com.openclassrooms.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(columnDefinition = "TEXT")
	private String message;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User userId;
	
	@ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
	private Rental rentalId;
}
