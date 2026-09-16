package com.travelit.trust.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePlaceSourceRequest {

    @NotNull(message = "Place ID is required")
    private Long placeId;

    @NotBlank(message = "Source name is required")
    @Size(max = 100, message = "Source name cannot exceed 100 characters")
    private String sourceName;

    @Size(max = 500, message = "Source URL cannot exceed 500 characters")
    private String sourceUrl;

    @Size(max = 1000, message = "Source description cannot exceed 1000 characters")
    private String sourceDescription;

    public CreatePlaceSourceRequest() {
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }
}