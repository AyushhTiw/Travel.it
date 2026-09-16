package com.travelit.category.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "place_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_place_category",
                        columnNames = {"place_id", "category_id"}
                )
        }
)
public class PlaceCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    public PlaceCategory() {
    }

    public PlaceCategory(Long placeId, Long categoryId) {
        this.placeId = placeId;
        this.categoryId = categoryId;
    }

    public Long getId() {
        return id;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}