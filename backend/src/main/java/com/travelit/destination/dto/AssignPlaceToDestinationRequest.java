package com.travelit.destination.dto;

import jakarta.validation.constraints.NotNull;

public class AssignPlaceToDestinationRequest {

    @NotNull(message = "Destination ID is required")
    private Long destinationId;

    @NotNull(message = "Place ID is required")
    private Long placeId;

    public AssignPlaceToDestinationRequest() {
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }
}