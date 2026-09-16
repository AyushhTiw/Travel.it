package com.travelit.destination.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "destinations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_destination_name_country",
                        columnNames = {"name", "country"}
                )
        }
)
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(length = 100)
    private String state;

    @Column(length = 1000)
    private String description;

    public Destination() {
    }

    public Destination(
            String name,
            String country,
            String state,
            String description
    ) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}