package com.openclassrooms.api.model;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;
    
    @Column(name="created_at", columnDefinition = "TIMESTAMP")
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    private Instant updatedAt;

}
