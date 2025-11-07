package com.openclassrooms.api.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(length = 2000)
	private String message;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User userId;
	
	@ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
	private Rental rentalId;

	@Column(name="created_at", columnDefinition = "TIMESTAMP")
	@CreationTimestamp
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private Instant updatedAt;
}
