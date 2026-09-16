package com.travelit.interaction.dto;

import java.time.LocalDateTime;

public class UserInteractionResponse {

    private Long id;
    private Long userId;
    private Long placeId;
    private String interactionType;
    private LocalDateTime createdAt;

    public UserInteractionResponse() {
    }

    public UserInteractionResponse(
            Long id,
            Long userId,
            Long placeId,
            String interactionType,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.interactionType = interactionType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}