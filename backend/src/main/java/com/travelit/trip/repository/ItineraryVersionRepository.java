package com.travelit.trip.repository;

import com.travelit.trip.entity.ItineraryVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItineraryVersionRepository
        extends JpaRepository<ItineraryVersion, Long> {

    List<ItineraryVersion> findByTripIdOrderByVersionNumberDesc(Long tripId);

    boolean existsByTripIdAndVersionNumber(
            Long tripId,
            Integer versionNumber
    );
}