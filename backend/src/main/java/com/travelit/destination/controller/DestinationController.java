package com.travelit.destination.controller;

import com.travelit.destination.dto.CreateDestinationRequest;
import com.travelit.destination.dto.DestinationResponse;
import com.travelit.destination.dto.UpdateDestinationRequest;
import com.travelit.destination.service.DestinationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(
            DestinationService destinationService
    ) {
        this.destinationService = destinationService;
    }

    @PostMapping
    public ResponseEntity<DestinationResponse> createDestination(
            @Valid @RequestBody CreateDestinationRequest request
    ) {
        return ResponseEntity.ok(
                destinationService.createDestination(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<DestinationResponse>> getAllDestinations(
            @RequestParam(required = false) String search
    ) {
        System.out.println("[DestinationController] GET /destinations - search=" + search);
        
        if (search != null && !search.trim().isEmpty()) {
            List<DestinationResponse> results = destinationService.searchDestinations(search);
            System.out.println("[DestinationController] Search returned " + results.size() + " results");
            return ResponseEntity.ok(results);
        }
        
        List<DestinationResponse> all = destinationService.getAllDestinations();
        System.out.println("[DestinationController] GetAll returned " + all.size() + " results");
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponse> getDestination(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                destinationService.getDestination(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationResponse> updateDestination(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDestinationRequest request
    ) {
        return ResponseEntity.ok(
                destinationService.updateDestination(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(
            @PathVariable Long id
    ) {
        destinationService.deleteDestination(id);

        return ResponseEntity.noContent().build();
    }
}
