package com.travelit.interaction.service;

import com.travelit.auth.entity.User;
import com.travelit.interaction.dto.CreateInteractionRequest;
import com.travelit.interaction.dto.UserInteractionResponse;
import com.travelit.interaction.entity.InteractionType;
import com.travelit.interaction.entity.UserInteraction;
import com.travelit.interaction.repository.UserInteractionRepository;
import com.travelit.place.service.PlaceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInteractionService {

    private final UserInteractionRepository interactionRepository;
    private final PlaceService placeService;

    public UserInteractionService(
            UserInteractionRepository interactionRepository,
            PlaceService placeService
    ) {
        this.interactionRepository = interactionRepository;
        this.placeService = placeService;
    }

    public UserInteractionResponse createInteraction(
            Authentication authentication,
            CreateInteractionRequest request
    ) {

        User user = getAuthenticatedUser(authentication);

        InteractionType type = parseType(
                request.getInteractionType()
        );

        // Verify that the place exists.
        placeService.getPlace(request.getPlaceId());

        UserInteraction interaction =
                interactionRepository
                        .findByUserIdAndPlaceIdAndInteractionType(
                                user.getId(),
                                request.getPlaceId(),
                                type.name()
                        )
                        .orElseGet(() ->
                                new UserInteraction(
                                        user.getId(),
                                        request.getPlaceId(),
                                        type.name()
                                )
                        );

        UserInteraction savedInteraction =
                interactionRepository.save(interaction);

        return toResponse(savedInteraction);
    }

    public List<UserInteractionResponse> getMyInteractions(
            Authentication authentication,
            String interactionType
    ) {

        User user = getAuthenticatedUser(authentication);

        InteractionType type = parseType(interactionType);

        return interactionRepository
                .findByUserIdAndInteractionType(
                        user.getId(),
                        type.name()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteInteraction(
            Authentication authentication,
            Long placeId,
            String interactionType
    ) {

        User user = getAuthenticatedUser(authentication);

        InteractionType type = parseType(interactionType);

        UserInteraction interaction =
                interactionRepository
                        .findByUserIdAndPlaceIdAndInteractionType(
                                user.getId(),
                                placeId,
                                type.name()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Interaction not found"
                                )
                        );

        interactionRepository.delete(interaction);
    }

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            throw new IllegalArgumentException(
                    "Authenticated user not found"
            );
        }

        return (User) authentication.getPrincipal();
    }

    private InteractionType parseType(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Interaction type is required"
            );
        }

        try {
            return InteractionType.valueOf(
                    value.trim().toUpperCase()
            );
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Invalid interaction type. Allowed values: SAVED, WISHLIST, VISITED, SKIPPED"
            );
        }
    }

    private UserInteractionResponse toResponse(
            UserInteraction interaction
    ) {

        return new UserInteractionResponse(
                interaction.getId(),
                interaction.getUserId(),
                interaction.getPlaceId(),
                interaction.getInteractionType(),
                interaction.getCreatedAt()
        );
    }
}