package com.travelit.place.controller;

import com.travelit.place.dto.CreatePlaceRequest;
import com.travelit.place.dto.GooglePlaceDetailsResponse;
import com.travelit.place.dto.GooglePlaceResponse;
import com.travelit.place.dto.PlaceResponse;
import com.travelit.place.dto.UpdatePlaceRequest;
import com.travelit.place.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping
    public ResponseEntity<PlaceResponse> createPlace(@Valid @RequestBody CreatePlaceRequest request) {
        return ResponseEntity.ok(placeService.createPlace(request));
    }

    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlace(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlace(id));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<PlaceResponse>> getPlacesByCity(@PathVariable String city) {
        return ResponseEntity.ok(placeService.getPlacesByCity(city));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<PlaceResponse>> getPlacesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(placeService.getPlacesByCountry(country));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PlaceResponse>> getActivePlaces() {
        return ResponseEntity.ok(placeService.getActivePlaces());
    }

    @GetMapping("/google-search")
    public ResponseEntity<List<GooglePlaceResponse>> searchGooglePlaces(
            @RequestParam(name = "query") String query) {
        return ResponseEntity.ok(placeService.searchGooglePlaces(query));
    }

    /**
     * Places page search — text query + optional category filter.
     * Powers the Places page with real Google Places data.
     * GET /api/v1/places/search?q=Lal+Qila&category=tourist_spots
     */
    @GetMapping("/search")
    public ResponseEntity<List<GooglePlaceResponse>> searchPlaces(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false, defaultValue = "all") String category) {
        return ResponseEntity.ok(placeService.searchPlacesPage(q, category));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<GooglePlaceResponse>> searchNearbyPlaces(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5000") double radius,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(
                placeService.searchNearbyPlaces(latitude, longitude, radius, category));
    }

    @GetMapping("/google-details/{placeId}")
    public ResponseEntity<GooglePlaceDetailsResponse> getGooglePlaceDetails(
            @PathVariable String placeId) {
        return ResponseEntity.ok(placeService.getGooglePlaceDetails(placeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponse> updatePlace(
            @PathVariable Long id, @Valid @RequestBody UpdatePlaceRequest request) {
        return ResponseEntity.ok(placeService.updatePlace(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}