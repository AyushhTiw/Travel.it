package com.travelit.trust.repository;

import com.travelit.trust.entity.PlaceSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceSourceRepository
        extends JpaRepository<PlaceSource, Long> {

    List<PlaceSource> findByPlaceIdOrderByCreatedAtDesc(
            Long placeId
    );
}