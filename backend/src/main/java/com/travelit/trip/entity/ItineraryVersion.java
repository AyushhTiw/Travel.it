package com.travelit.trip.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "itinerary_versions")
public class ItineraryVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private Integer versionNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(length = 1000)
    private String description;

    public ItineraryVersion() {
    }

    public ItineraryVersion(
            Trip trip,
            Integer versionNumber,
            String description
    ) {
        this.trip = trip;
        this.versionNumber = versionNumber;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
