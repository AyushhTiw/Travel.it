package com.travelit.trip.controller;

import com.travelit.trip.dto.CreateTripDayRequest;
import com.travelit.trip.dto.TripDayResponse;
import com.travelit.trip.dto.UpdateTripDayRequest;
import com.travelit.trip.service.TripDayService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/days")
public class TripDayController {

    private final TripDayService tripDayService;

    public TripDayController(TripDayService tripDayService) {
        this.tripDayService = tripDayService;
    }

    @PostMapping
    public ResponseEntity<TripDayResponse> createTripDay(
            @PathVariable Long tripId,
            @Valid @RequestBody CreateTripDayRequest request
    ) {

        return ResponseEntity.ok(
                tripDayService.createTripDay(
                        tripId,
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<TripDayResponse>> getTripDays(
            @PathVariable Long tripId
    ) {

        return ResponseEntity.ok(
                tripDayService.getTripDays(tripId)
        );
    }

    @GetMapping("/{dayId}")
    public ResponseEntity<TripDayResponse> getTripDay(
            @PathVariable Long tripId,
            @PathVariable Long dayId
    ) {

        return ResponseEntity.ok(
                tripDayService.getTripDay(
                        tripId,
                        dayId
                )
        );
    }

    @PutMapping("/{dayId}")
    public ResponseEntity<TripDayResponse> updateTripDay(
            @PathVariable Long tripId,
            @PathVariable Long dayId,
            @Valid @RequestBody UpdateTripDayRequest request
    ) {

        return ResponseEntity.ok(
                tripDayService.updateTripDay(
                        tripId,
                        dayId,
                        request
                )
        );
    }

    @DeleteMapping("/{dayId}")
    public ResponseEntity<Void> deleteTripDay(
            @PathVariable Long tripId,
            @PathVariable Long dayId
    ) {

        tripDayService.deleteTripDay(
                tripId,
                dayId
        );

        return ResponseEntity.noContent().build();
    }
}