package com.travelit.trip.dto;

import java.time.LocalTime;

public class ItineraryItemResponse {

    private Long id;
    private Long tripDayId;
    private String title;
    private String description;
    private String location;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer sortOrder;

    public ItineraryItemResponse() {
    }

    public ItineraryItemResponse(
            Long id,
            Long tripDayId,
            String title,
            String description,
            String location,
            LocalTime startTime,
            LocalTime endTime,
            Integer sortOrder
    ) {
        this.id = id;
        this.tripDayId = tripDayId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getTripDayId() {
        return tripDayId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}