package com.travelit.interaction.controller;

import com.travelit.interaction.dto.CreateInteractionRequest;
import com.travelit.interaction.dto.UserInteractionResponse;
import com.travelit.interaction.service.UserInteractionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interactions")
public class UserInteractionController {

    private final UserInteractionService interactionService;

    public UserInteractionController(
            UserInteractionService interactionService
    ) {
        this.interactionService = interactionService;
    }

    @PostMapping
    public ResponseEntity<UserInteractionResponse> createInteraction(
            Authentication authentication,
            @Valid @RequestBody CreateInteractionRequest request
    ) {

        return ResponseEntity.ok(
                interactionService.createInteraction(
                        authentication,
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<UserInteractionResponse>> getMyInteractions(
            Authentication authentication,
            @RequestParam String type
    ) {

        return ResponseEntity.ok(
                interactionService.getMyInteractions(
                        authentication,
                        type
                )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteInteraction(
            Authentication authentication,
            @RequestParam Long placeId,
            @RequestParam String type
    ) {

        interactionService.deleteInteraction(
                authentication,
                placeId,
                type
        );

        return ResponseEntity.noContent().build();
    }
}