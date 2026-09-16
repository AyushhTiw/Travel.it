package com.travelit.interaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateInteractionRequest {

    @NotNull(message = "Place ID is required")
    private Long placeId;

    @NotBlank(message = "Interaction type is required")
    @Size(
            max = 30,
            message = "Interaction type cannot exceed 30 characters"
    )
    private String interactionType;

    public CreateInteractionRequest() {
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }
}