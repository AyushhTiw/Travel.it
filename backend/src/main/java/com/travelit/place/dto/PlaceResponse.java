package com.travelit.place.dto;

import java.math.BigDecimal;

public class PlaceResponse {

    private Long id;
    private String name;
    private String description;
    private String city;
    private String state;
    private String country;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private boolean active;

    public PlaceResponse() {
    }

    public PlaceResponse(
            Long id,
            String name,
            String description,
            String city,
            String state,
            String country,
            BigDecimal latitude,
            BigDecimal longitude,
            String address,
            boolean active
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.state = state;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public boolean isActive() {
        return active;
    }
}