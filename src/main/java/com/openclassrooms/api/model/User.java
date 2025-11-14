package com.openclassrooms.api.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "User ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

	@NotBlank
	@Schema(description = "User name", example = "Alex")
    private String name;
	
	@NotBlank
	@NotNull
	@Schema(description = "User email", example = "test@test.com")
    private String email;
    
    // avoid sending password during response
	@NotBlank
	@NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Schema(description = "User password, which is stored in crypted form", example = "$2a$10$Wi0nDGlPqnlbbETrAoqEEOdgWGPd47dUZMgw.exMDXgl6mOAvtz0K")
    private String password;
    
    @Column(name="created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    @Schema(description = "Date when user was created", example = "2025-11-13T16:01:16Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    @Schema(description = "Date when user was last updated", example = "2025-11-13T16:01:16Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;

}
