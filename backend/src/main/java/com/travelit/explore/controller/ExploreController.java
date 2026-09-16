package com.travelit.explore.controller;

import com.travelit.explore.dto.NearbyPlaceResponse;
import com.travelit.explore.service.ExploreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/explore")
public class ExploreController {

    private final ExploreService exploreService;

    public ExploreController(ExploreService exploreService) {
        this.exploreService = exploreService;
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyPlaceResponse>> findNearbyPlaces(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") double radiusKm
    ) {

        return ResponseEntity.ok(
                exploreService.findNearbyPlaces(
                        latitude,
                        longitude,
                        radiusKm
                )
        );
    }
}