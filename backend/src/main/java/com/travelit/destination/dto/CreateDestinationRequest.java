package com.travelit.destination.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateDestinationRequest {

    @NotBlank(message = "Destination name is required")
    @Size(max = 100, message = "Destination name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    public CreateDestinationRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}