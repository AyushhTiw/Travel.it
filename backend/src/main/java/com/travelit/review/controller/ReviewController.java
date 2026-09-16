package com.travelit.review.controller;

import com.travelit.review.dto.CreateReviewRequest;
import com.travelit.review.dto.PlaceReviewSummaryResponse;
import com.travelit.review.dto.ReviewResponse;
import com.travelit.review.dto.UpdateReviewRequest;
import com.travelit.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            Authentication authentication,
            @Valid @RequestBody CreateReviewRequest request
    ) {

        return ResponseEntity.ok(
                reviewService.createReview(
                        authentication,
                        request
                )
        );
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<ReviewResponse>> getPlaceReviews(
            @PathVariable Long placeId
    ) {

        return ResponseEntity.ok(
                reviewService.getPlaceReviews(placeId)
        );
    }

    @GetMapping("/place/{placeId}/summary")
    public ResponseEntity<PlaceReviewSummaryResponse>
    getPlaceReviewSummary(
            @PathVariable Long placeId
    ) {

        return ResponseEntity.ok(
                reviewService.getPlaceReviewSummary(placeId)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                reviewService.getMyReviews(authentication)
        );
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            Authentication authentication,
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request
    ) {

        return ResponseEntity.ok(
                reviewService.updateReview(
                        authentication,
                        reviewId,
                        request
                )
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId
    ) {

        reviewService.deleteReview(
                authentication,
                reviewId
        );

        return ResponseEntity.noContent().build();
    }
}