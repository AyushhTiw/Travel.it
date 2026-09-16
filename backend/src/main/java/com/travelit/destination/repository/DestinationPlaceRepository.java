package com.travelit.destination.repository;

import com.travelit.destination.entity.DestinationPlace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DestinationPlaceRepository
        extends JpaRepository<DestinationPlace, Long> {

    List<DestinationPlace> findByDestinationId(Long destinationId);

    List<DestinationPlace> findByPlaceId(Long placeId);

    Optional<DestinationPlace> findByDestinationIdAndPlaceId(
            Long destinationId,
            Long placeId
    );

    boolean existsByDestinationIdAndPlaceId(
            Long destinationId,
            Long placeId
    );
}