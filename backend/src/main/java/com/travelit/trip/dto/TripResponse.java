package com.travelit.trip.dto;

import java.time.LocalDate;

public class TripResponse {

    private Long id;
    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private boolean active;

    public TripResponse() {
    }

    public TripResponse(
            Long id,
            String title,
            String destination,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            boolean active
    ) {
        this.id = id;
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }
}