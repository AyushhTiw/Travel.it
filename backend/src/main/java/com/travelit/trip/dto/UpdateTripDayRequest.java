package com.travelit.trip.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateTripDayRequest {

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Day number is required")
    private Integer dayNumber;

    @Size(
            max = 500,
            message = "Notes cannot exceed 500 characters"
    )
    private String notes;

    public UpdateTripDayRequest() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}