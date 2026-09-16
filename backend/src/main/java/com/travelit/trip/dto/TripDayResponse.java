package com.travelit.trip.dto;

import java.time.LocalDate;

public class TripDayResponse {

    private Long id;
    private Long tripId;
    private LocalDate date;
    private Integer dayNumber;
    private String notes;

    public TripDayResponse() {
    }

    public TripDayResponse(
            Long id,
            Long tripId,
            LocalDate date,
            Integer dayNumber,
            String notes
    ) {
        this.id = id;
        this.tripId = tripId;
        this.date = date;
        this.dayNumber = dayNumber;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public Long getTripId() {
        return tripId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public String getNotes() {
        return notes;
    }
}