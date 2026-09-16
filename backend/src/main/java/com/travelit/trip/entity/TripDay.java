package com.travelit.trip.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "trip_days")
public class TripDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Integer dayNumber;

    @Column(length = 500)
    private String notes;

    public TripDay() {
    }

    public TripDay(
            Trip trip,
            LocalDate date,
            Integer dayNumber,
            String notes
    ) {
        this.trip = trip;
        this.date = date;
        this.dayNumber = dayNumber;
        this.notes = notes;
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