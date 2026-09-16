package com.travelit.destination.repository;

import com.travelit.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    Optional<Destination> findByNameIgnoreCaseAndCountryIgnoreCase(
            String name,
            String country
    );

    boolean existsByNameIgnoreCaseAndCountryIgnoreCase(
            String name,
            String country
    );
}