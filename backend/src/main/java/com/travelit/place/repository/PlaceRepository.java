package com.travelit.place.repository;

import com.travelit.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCityIgnoreCase(String city);

    List<Place> findByCountryIgnoreCase(String country);

    List<Place> findByActiveTrue();
}