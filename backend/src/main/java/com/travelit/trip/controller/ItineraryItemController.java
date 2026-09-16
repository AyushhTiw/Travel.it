package com.travelit.trip.controller;

import com.travelit.trip.dto.CreateItineraryItemRequest;
import com.travelit.trip.dto.ItineraryItemResponse;
import com.travelit.trip.dto.UpdateItineraryItemRequest;
import com.travelit.trip.service.ItineraryItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip-days/{tripDayId}/items")
public class ItineraryItemController {

    private final ItineraryItemService itineraryItemService;

    public ItineraryItemController(
            ItineraryItemService itineraryItemService
    ) {
        this.itineraryItemService = itineraryItemService;
    }

    @PostMapping
    public ResponseEntity<ItineraryItemResponse> createItem(
            @PathVariable Long tripDayId,
            @Valid @RequestBody CreateItineraryItemRequest request
    ) {
        return ResponseEntity.ok(
                itineraryItemService.createItem(tripDayId, request)
        );
    }

    @GetMapping
    public ResponseEntity<List<ItineraryItemResponse>> getItems(
            @PathVariable Long tripDayId
    ) {
        return ResponseEntity.ok(
                itineraryItemService.getItems(tripDayId)
        );
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItineraryItemResponse> getItem(
            @PathVariable Long tripDayId,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                itineraryItemService.getItem(tripDayId, itemId)
        );
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItineraryItemResponse> updateItem(
            @PathVariable Long tripDayId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateItineraryItemRequest request
    ) {
        return ResponseEntity.ok(
                itineraryItemService.updateItem(
                        tripDayId,
                        itemId,
                        request
                )
        );
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long tripDayId,
            @PathVariable Long itemId
    ) {
        itineraryItemService.deleteItem(tripDayId, itemId);

        return ResponseEntity.noContent().build();
    }
}