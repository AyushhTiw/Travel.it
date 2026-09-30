package com.travelit.trip.repository;

import com.travelit.trip.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUserIdOrderByStartDateDesc(Long userId);

    List<Trip> findByUserIdAndActiveTrueOrderByStartDateDesc(Long userId);

    Optional<Trip> findByIdAndUserId(Long id, Long userId);

    List<Trip> findByActiveTrue();

    List<Trip> findByDestinationIgnoreCase(String destination);
}