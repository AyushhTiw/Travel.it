package com.travelit.category.dto;

public class PlaceCategoryResponse {

    private Long id;
    private Long placeId;
    private Long categoryId;

    public PlaceCategoryResponse() {
    }

    public PlaceCategoryResponse(
            Long id,
            Long placeId,
            Long categoryId
    ) {
        this.id = id;
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