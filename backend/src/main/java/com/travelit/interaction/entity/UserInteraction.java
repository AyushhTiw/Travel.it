package com.travelit.interaction.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_interactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_place_interaction",
                        columnNames = {"user_id", "place_id", "interaction_type"}
                )
        }
)
public class UserInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(
            name = "interaction_type",
            nullable = false,
            length = 30
    )
    private String interactionType;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public UserInteraction() {
    }

    public UserInteraction(
            Long userId,
            Long placeId,
            String interactionType
    ) {
        this.userId = userId;
        this.placeId = placeId;
        this.interactionType = interactionType;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}