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
	
	@Column(name = "user_id", nullable = false)
	private Integer user_id;
	
	@Column(name = "rental_id", nullable = false)
	private Integer rental_id;

	@Column(name="created_at", columnDefinition = "TIMESTAMP")
	@CreationTimestamp
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private Instant updatedAt;
}
