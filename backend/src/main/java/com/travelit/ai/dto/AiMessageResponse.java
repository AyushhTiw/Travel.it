package com.travelit.ai.dto;

import com.travelit.ai.entity.AiMessage;

import java.time.LocalDateTime;

public class AiMessageResponse {

    private Long id;
    private Long conversationId;
    private String role;
    private String content;
    private LocalDateTime createdAt;

    public AiMessageResponse() {
    }

    public AiMessageResponse(
            Long id,
            Long conversationId,
            String role,
            String content,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static AiMessageResponse from(AiMessage message) {
        return new AiMessageResponse(
                message.getId(),
                message.getConversationId(),
                message.getRole(),
                message.getContent(),
                message.getCreatedAt()
        );
    }

    // Getters
    public Long getId() { return id; }
    public Long getConversationId() { return conversationId; }
    public String getRole() { return role; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters (needed for transient/guest responses built without an entity)
    public void setId(Long id) { this.id = id; }
    public void setConversationId(Long conversationId) { this.conversationId = conversationId; }
    public void setRole(String role) { this.role = role; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
