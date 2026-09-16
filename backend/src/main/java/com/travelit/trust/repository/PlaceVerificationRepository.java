package com.travelit.trust.repository;

import com.travelit.trust.entity.PlaceVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceVerificationRepository
        extends JpaRepository<PlaceVerification, Long> {

    List<PlaceVerification>
    findByPlaceIdOrderByVerifiedAtDesc(Long placeId);

    List<PlaceVerification>
    findByPlaceIdAndVerifiedTrue(Long placeId);
}