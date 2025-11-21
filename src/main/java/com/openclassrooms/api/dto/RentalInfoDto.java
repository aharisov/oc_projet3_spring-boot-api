package com.openclassrooms.api.dto;

public class RentalInfoDto {
	private Integer id;
	private String name;
    private Integer surface;
    private Integer price;
    private String picture;
    private String description;
    private Integer owner_id;
    private String created_at;
    private String updated_at;

    // REQUIRED for ModelMapper
    public RentalInfoDto() {}
    
    // Constructor
    public RentalInfoDto(Integer id, String name, Integer surface, Integer price, String picture, 
    		String description, Integer owner_id, String created_at, String updated_at) {
        this.id = id;
    	this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.owner_id = owner_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getSurface() { return surface; }
    public void setSurface(Integer surface) { this.surface = surface; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getOwner_id() { return owner_id; }
    public void setOwner_id(Integer owner_id) { this.owner_id = owner_id; }
    
    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
    
    public String getUpdated_at() { return updated_at; }
    public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
}
