package com.travelit.destination.dto;

public class DestinationResponse {

    private Long id;
    private String name;
    private String country;
    private String state;
    private String description;

    public DestinationResponse() {
    }

    public DestinationResponse(
            Long id,
            String name,
            String country,
            String state,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.state = state;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}