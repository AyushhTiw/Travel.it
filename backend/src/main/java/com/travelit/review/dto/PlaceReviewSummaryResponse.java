package com.travelit.review.dto;

public class PlaceReviewSummaryResponse {

    private Long placeId;
    private long totalReviews;
    private double averageRating;

    public PlaceReviewSummaryResponse() {
    }

    public PlaceReviewSummaryResponse(
            Long placeId,
            long totalReviews,
            double averageRating
    ) {
        this.placeId = placeId;
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public double getAverageRating() {
        return averageRating;
    }
}