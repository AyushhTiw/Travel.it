package com.travelit.trust.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "place_verifications")
public class PlaceVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(nullable = false, length = 50)
    private String verificationType;

    @Column(nullable = false)
    private boolean verified;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime verifiedAt;

    public PlaceVerification() {
    }

    public PlaceVerification(
            Long placeId,
            String verificationType,
            boolean verified,
            String notes
    ) {
        this.placeId = placeId;
        this.verificationType = verificationType;
        this.verified = verified;
        this.notes = notes;
        this.verifiedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
}