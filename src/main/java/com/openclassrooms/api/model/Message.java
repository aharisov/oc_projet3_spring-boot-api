package com.openclassrooms.api.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Message ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;
	
	@Column(length = 2000)
	@NotBlank
	@NotNull
	@Size(max = 2000)
    @Schema(description = "Message text", example = "Mon message")
	private String message;
	
	@NotBlank
	@NotNull
	@Column(name = "user_id", nullable = false)
	@Schema(description = "ID of the message owner", example = "1")
	private Integer user_id;
	
	@NotBlank
	@NotNull
	@Column(name = "rental_id", nullable = false)
	@Schema(description = "ID of the rental to which belongs message", example = "2")
	private Integer rental_id;

	@Column(name="created_at", columnDefinition = "TIMESTAMP")
	@CreationTimestamp
	@Schema(description = "Date when message was created", example = "2025-11-13T16:01:16Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    @Schema(description = "Date when message was updated", example = "2025-11-13T16:01:16Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;
}
