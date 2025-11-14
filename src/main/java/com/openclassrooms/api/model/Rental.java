package com.openclassrooms.api.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "rentals")
public class Rental {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Rental ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

	@Schema(description = "Rental name", example = "New apartment")
    private String name;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Rental surface in m²", example = "80.5")
    private BigDecimal surface;

    @Column(precision = 10, scale = 2)
    @Schema(description = "Rental price in euro", example = "1200.00")
    private BigDecimal price;

    @Schema(description = "URL to rental picture", example = "http://localhost:8080/api/uploads/image.jpg")
    private String picture;
    
    @Column(length = 2000)
    @Size(max = 2000)
    @Schema(description = "Rental description", example = "A spacious apartment with 2 bedrooms and 1 bathroom")
    private String description;
    
    @Column(name = "owner_id", nullable = false)
    @Schema(description = "ID of the rental owner", example = "2")
    private Integer ownerId;
    
    @Column(name="created_at", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    @Schema(description = "Date when rental was created", example = "2025-11-13T16:01:16Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant createdAt;
    
    @Column(name="updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    @Schema(description = "Date when rental was last updated", example = "2025-11-13T16:01:16Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Instant updatedAt;
}
