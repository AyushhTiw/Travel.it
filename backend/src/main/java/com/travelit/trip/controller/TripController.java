package com.travelit.trip.controller;

import com.travelit.trip.dto.CreateTripRequest;
import com.travelit.trip.dto.TripResponse;
import com.travelit.trip.dto.UpdateTripRequest;
import com.travelit.trip.service.TripService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(
            @Valid @RequestBody CreateTripRequest request
    ) {

        return ResponseEntity.ok(
                tripService.createTrip(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<TripResponse>> getAllTrips() {

        return ResponseEntity.ok(
                tripService.getAllTrips()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTrip(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                tripService.getTrip(id)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<TripResponse>> getActiveTrips() {

        return ResponseEntity.ok(
                tripService.getActiveTrips()
        );
    }

    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<TripResponse>> getTripsByDestination(
            @PathVariable String destination
    ) {

        return ResponseEntity.ok(
                tripService.getTripsByDestination(destination)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponse> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTripRequest request
    ) {

        return ResponseEntity.ok(
                tripService.updateTrip(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id
    ) {

        tripService.deleteTrip(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateTrip(
            @PathVariable Long id
    ) {

        tripService.deactivateTrip(id);

        return ResponseEntity.noContent().build();
    }
}