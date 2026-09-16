package com.travelit.category.dto;

import jakarta.validation.constraints.NotNull;

public class AssignCategoryRequest {

    @NotNull(message = "Place ID is required")
    private Long placeId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    public AssignCategoryRequest() {
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}