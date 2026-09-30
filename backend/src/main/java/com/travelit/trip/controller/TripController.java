package com.travelit.trip.controller;
import com.travelit.trip.dto.CreateTripRequest;
import com.travelit.trip.dto.TripResponse;
import com.travelit.trip.dto.UpdateTripRequest;
import com.travelit.trip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private final TripService tripService;
    public TripController(TripService tripService) { this.tripService = tripService; }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@Valid @RequestBody CreateTripRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(request, auth));
    }

    @GetMapping
    public ResponseEntity<List<TripResponse>> getMyTrips(Authentication auth) {
        return ResponseEntity.ok(tripService.getMyTrips(auth));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(tripService.getTrip(id, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable Long id, @Valid @RequestBody UpdateTripRequest request, Authentication auth) {
        return ResponseEntity.ok(tripService.updateTrip(id, request, auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id, Authentication auth) {
        tripService.deleteTrip(id, auth);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateTrip(@PathVariable Long id, Authentication auth) {
        tripService.deactivateTrip(id, auth);
        return ResponseEntity.noContent().build();
    }
}
