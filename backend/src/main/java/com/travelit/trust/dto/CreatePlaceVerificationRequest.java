package com.travelit.trust.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePlaceVerificationRequest {

    @NotNull(message = "Place ID is required")
    private Long placeId;

    @NotBlank(message = "Verification type is required")
    @Size(max = 50, message = "Verification type cannot exceed 50 characters")
    private String verificationType;

    @NotNull(message = "Verified status is required")
    private Boolean verified;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    public CreatePlaceVerificationRequest() {
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}