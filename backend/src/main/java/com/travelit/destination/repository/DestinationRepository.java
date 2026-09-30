package com.travelit.destination.repository;

import com.travelit.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    /**
     * Search destinations by name, country, state, or description (case-insensitive, partial match).
     */
    @Query("SELECT d FROM Destination d WHERE " +
           "LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.country) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Destination> searchDestinations(@Param("searchTerm") String searchTerm);
}
