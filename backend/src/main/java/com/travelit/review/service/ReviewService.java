package com.travelit.review.service;

import com.travelit.auth.entity.User;
import com.travelit.place.service.PlaceService;
import com.travelit.review.dto.CreateReviewRequest;
import com.travelit.review.dto.PlaceReviewSummaryResponse;
import com.travelit.review.dto.ReviewResponse;
import com.travelit.review.dto.UpdateReviewRequest;
import com.travelit.review.entity.Review;
import com.travelit.review.repository.ReviewRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceService placeService;

    public ReviewService(
            ReviewRepository reviewRepository,
            PlaceService placeService
    ) {
        this.reviewRepository = reviewRepository;
        this.placeService = placeService;
    }

    public ReviewResponse createReview(
            Authentication authentication,
            CreateReviewRequest request
    ) {

        User user = getAuthenticatedUser(authentication);

        // Verify that the place exists.
        placeService.getPlace(request.getPlaceId());

        if (reviewRepository.existsByUserIdAndPlaceId(
                user.getId(),
                request.getPlaceId()
        )) {
            throw new IllegalArgumentException(
                    "You have already reviewed this place"
            );
        }

        Review review = new Review(
                user.getId(),
                request.getPlaceId(),
                request.getRating(),
                clean(request.getComment())
        );

        return toResponse(
                reviewRepository.save(review)
        );
    }

    public List<ReviewResponse> getPlaceReviews(
            Long placeId
    ) {

        // Verify that the place exists.
        placeService.getPlace(placeId);

        return reviewRepository
                .findByPlaceIdOrderByCreatedAtDesc(placeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ReviewResponse> getMyReviews(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return reviewRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ReviewResponse updateReview(
            Authentication authentication,
            Long reviewId,
            UpdateReviewRequest request
    ) {

        User user = getAuthenticatedUser(authentication);

        Review review = findReview(reviewId);

        if (!review.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "You can only update your own review"
            );
        }

        review.setRating(request.getRating());
        review.setComment(clean(request.getComment()));
        review.setUpdatedAt(java.time.LocalDateTime.now());

        return toResponse(
                reviewRepository.save(review)
        );
    }

    public void deleteReview(
            Authentication authentication,
            Long reviewId
    ) {

        User user = getAuthenticatedUser(authentication);

        Review review = findReview(reviewId);

        if (!review.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "You can only delete your own review"
            );
        }

        reviewRepository.delete(review);
    }

    public PlaceReviewSummaryResponse getPlaceReviewSummary(
            Long placeId
    ) {

        // Verify that the place exists.
        placeService.getPlace(placeId);

        List<Review> reviews =
                reviewRepository.findByPlaceIdOrderByCreatedAtDesc(
                        placeId
                );

        if (reviews.isEmpty()) {
            return new PlaceReviewSummaryResponse(
                    placeId,
                    0,
                    0.0
            );
        }

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        averageRating =
                Math.round(averageRating * 100.0) / 100.0;

        return new PlaceReviewSummaryResponse(
                placeId,
                reviews.size(),
                averageRating
        );
    }

    private Review findReview(Long reviewId) {

        return reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Review not found"
                        ));
    }

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            throw new IllegalArgumentException(
                    "Authenticated user not found"
            );
        }

        return (User) authentication.getPrincipal();
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        String cleaned = value.trim();

        return cleaned.isEmpty() ? null : cleaned;
    }

    private ReviewResponse toResponse(Review review) {

        return new ReviewResponse(
                review.getId(),
                review.getUserId(),
                review.getPlaceId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}