package com.openclassrooms.api.dto;

public class UpdateRentalDto {
	private Integer id;
	private String name;
    private String surface;
    private String price;
    private String picture;
    private String description;
    private Integer owner_id;

    // Constructor
    public UpdateRentalDto(Integer id, String name, String surface, String price, String picture, String description, Integer owner_id) {
        this.id = id;
    	this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.owner_id = owner_id;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurface() { return surface; }
    public void setSurface(String surface) { this.surface = surface; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getOwner_id() { return owner_id; }
    public void setOwner_id(Integer owner_id) { this.owner_id = owner_id; }
}
