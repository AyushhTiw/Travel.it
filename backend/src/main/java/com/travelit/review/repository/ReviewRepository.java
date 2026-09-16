package com.travelit.review.repository;

import com.travelit.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByPlaceIdOrderByCreatedAtDesc(Long placeId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Review> findByUserIdAndPlaceId(
            Long userId,
            Long placeId
    );

    boolean existsByUserIdAndPlaceId(
            Long userId,
            Long placeId
    );
}