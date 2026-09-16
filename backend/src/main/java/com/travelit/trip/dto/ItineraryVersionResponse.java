package com.travelit.trip.dto;

import java.time.LocalDateTime;

public class ItineraryVersionResponse {

    private Long id;
    private Long tripId;
    private Integer versionNumber;
    private LocalDateTime createdAt;
    private String description;

    public ItineraryVersionResponse() {
    }

    public ItineraryVersionResponse(
            Long id,
            Long tripId,
            Integer versionNumber,
            LocalDateTime createdAt,
            String description
    ) {
        this.id = id;
        this.tripId = tripId;
        this.versionNumber = versionNumber;
        this.createdAt = createdAt;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }
}