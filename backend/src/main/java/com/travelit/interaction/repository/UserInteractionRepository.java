package com.travelit.interaction.repository;

import com.travelit.interaction.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInteractionRepository
        extends JpaRepository<UserInteraction, Long> {

    Optional<UserInteraction>
    findByUserIdAndPlaceIdAndInteractionType(
            Long userId,
            Long placeId,
            String interactionType
    );

    List<UserInteraction>
    findByUserIdAndInteractionType(
            Long userId,
            String interactionType
    );

    boolean
    existsByUserIdAndPlaceIdAndInteractionType(
            Long userId,
            Long placeId,
            String interactionType
    );
}