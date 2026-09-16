package com.travelit.trust.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "place_sources")
public class PlaceSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(nullable = false, length = 100)
    private String sourceName;

    @Column(length = 500)
    private String sourceUrl;

    @Column(length = 1000)
    private String sourceDescription;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public PlaceSource() {
    }

    public PlaceSource(
            Long placeId,
            String sourceName,
            String sourceUrl,
            String sourceDescription
    ) {
        this.placeId = placeId;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.sourceDescription = sourceDescription;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getPlaceId() {
        return placeId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}