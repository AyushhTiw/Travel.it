package com.travelit.trip.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "itinerary_items")
public class ItineraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_day_id", nullable = false)
    private TripDay tripDay;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String location;

    private LocalTime startTime;

    private LocalTime endTime;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    public ItineraryItem() {
    }

    public ItineraryItem(
            TripDay tripDay,
            String title,
            String description,
            String location,
            LocalTime startTime,
            LocalTime endTime,
            Integer sortOrder
    ) {
        this.tripDay = tripDay;
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

    public TripDay getTripDay() {
        return tripDay;
    }

    public void setTripDay(TripDay tripDay) {
        this.tripDay = tripDay;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}