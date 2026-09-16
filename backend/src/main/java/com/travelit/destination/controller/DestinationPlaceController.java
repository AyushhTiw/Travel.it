package com.travelit.destination.controller;

import com.travelit.destination.dto.AssignPlaceToDestinationRequest;
import com.travelit.destination.entity.DestinationPlace;
import com.travelit.destination.service.DestinationPlaceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationPlaceController {

    private final DestinationPlaceService destinationPlaceService;

    public DestinationPlaceController(
            DestinationPlaceService destinationPlaceService
    ) {
        this.destinationPlaceService = destinationPlaceService;
    }

    @PostMapping("/places")
    public ResponseEntity<DestinationPlace> assignPlace(
            @Valid @RequestBody AssignPlaceToDestinationRequest request
    ) {
        return ResponseEntity.ok(
                destinationPlaceService.assignPlace(request)
        );
    }

    @GetMapping("/{destinationId}/places")
    public ResponseEntity<List<DestinationPlace>> getPlacesForDestination(
            @PathVariable Long destinationId
    ) {
        return ResponseEntity.ok(
                destinationPlaceService.getPlacesForDestination(
                        destinationId
                )
        );
    }

    @GetMapping("/places/{placeId}/destinations")
    public ResponseEntity<List<DestinationPlace>> getDestinationsForPlace(
            @PathVariable Long placeId
    ) {
        return ResponseEntity.ok(
                destinationPlaceService.getDestinationsForPlace(
                        placeId
                )
        );
    }

    @DeleteMapping("/{destinationId}/places/{placeId}")
    public ResponseEntity<Void> removePlaceFromDestination(
            @PathVariable Long destinationId,
            @PathVariable Long placeId
    ) {
        destinationPlaceService.removePlaceFromDestination(
                destinationId,
                placeId
        );

        return ResponseEntity.noContent().build();
    }
}