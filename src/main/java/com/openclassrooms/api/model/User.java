package com.openclassrooms.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    
    @Column(name="created_at")
    private String createdAt;
    
    @Column(name="updated_at")
    private String updatedAt;

}
