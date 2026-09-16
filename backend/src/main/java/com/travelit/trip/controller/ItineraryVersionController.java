package com.travelit.trip.controller;

import com.travelit.trip.dto.CreateItineraryVersionRequest;
import com.travelit.trip.dto.ItineraryVersionResponse;
import com.travelit.trip.service.ItineraryVersionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/versions")
public class ItineraryVersionController {

    private final ItineraryVersionService versionService;

    public ItineraryVersionController(
            ItineraryVersionService versionService
    ) {
        this.versionService = versionService;
    }

    @PostMapping
    public ResponseEntity<ItineraryVersionResponse> createVersion(
            @PathVariable Long tripId,
            @Valid @RequestBody CreateItineraryVersionRequest request
    ) {

        return ResponseEntity.ok(
                versionService.createVersion(
                        tripId,
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<ItineraryVersionResponse>> getVersions(
            @PathVariable Long tripId
    ) {

        return ResponseEntity.ok(
                versionService.getVersions(tripId)
        );
    }

    @GetMapping("/{versionId}")
    public ResponseEntity<ItineraryVersionResponse> getVersion(
            @PathVariable Long tripId,
            @PathVariable Long versionId
    ) {

        return ResponseEntity.ok(
                versionService.getVersion(
                        tripId,
                        versionId
                )
        );
    }

    @DeleteMapping("/{versionId}")
    public ResponseEntity<Void> deleteVersion(
            @PathVariable Long tripId,
            @PathVariable Long versionId
    ) {

        versionService.deleteVersion(
                tripId,
                versionId
        );

        return ResponseEntity.noContent().build();
    }
}