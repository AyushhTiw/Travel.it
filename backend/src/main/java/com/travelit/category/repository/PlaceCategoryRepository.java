package com.travelit.category.repository;

import com.travelit.category.entity.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceCategoryRepository
        extends JpaRepository<PlaceCategory, Long> {

    List<PlaceCategory> findByPlaceId(Long placeId);

    List<PlaceCategory> findByCategoryId(Long categoryId);

    Optional<PlaceCategory> findByPlaceIdAndCategoryId(
            Long placeId,
            Long categoryId
    );

    boolean existsByPlaceIdAndCategoryId(
            Long placeId,
            Long categoryId
    );
}