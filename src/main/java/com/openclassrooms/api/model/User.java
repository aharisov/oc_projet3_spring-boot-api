package com.openclassrooms.api.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    // avoid sending password during response
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Column(name="created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private Instant updatedAt;

}
