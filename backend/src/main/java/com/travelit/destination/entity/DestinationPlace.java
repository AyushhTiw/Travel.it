package com.travelit.destination.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "destination_places",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_destination_place",
                        columnNames = {"destination_id", "place_id"}
                )
        }
)
public class DestinationPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destination_id", nullable = false)
    private Long destinationId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    public DestinationPlace() {
    }

    public DestinationPlace(Long destinationId, Long placeId) {
        this.destinationId = destinationId;
        this.placeId = placeId;
    }

    public Long getId() {
        return id;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Long getPlaceId() {
        return placeId;
    }
}