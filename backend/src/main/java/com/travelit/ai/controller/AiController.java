package com.travelit.ai.controller;

import com.travelit.ai.dto.AiConversationResponse;
import com.travelit.ai.dto.AiMessageResponse;
import com.travelit.ai.dto.CreateConversationRequest;
import com.travelit.ai.dto.SendMessageRequest;
import com.travelit.ai.service.AiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI / Trevvy endpoints.
 *
 * ALL endpoints are publicly accessible (no login required).
 * SecurityConfig permits /api/v1/ai/** without authentication.
 *
 * When a JWT is present the user is identified and conversation history
 * is persisted in MySQL under their account.
 *
 * When no JWT is present (guest) the endpoint still works:
 *   - Guest conversations are stateless (prompt is answered without DB persistence).
 *   - The Authentication parameter will be null; AiService handles this gracefully.
 */
@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    // ────────────────────────────────────────────────────────────
    // Conversations (authenticated users only — guests skip these)
    // ────────────────────────────────────────────────────────────

    @PostMapping("/conversations")
    public ResponseEntity<AiConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(aiService.createConversation(request, authentication));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<AiConversationResponse>> getMyConversations(
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(aiService.getMyConversations(authentication));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<AiMessageResponse>> getMessages(
            @PathVariable Long conversationId,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(aiService.getMessages(conversationId, authentication));
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<AiMessageResponse> sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(aiService.sendMessage(conversationId, request, authentication));
    }

    // ────────────────────────────────────────────────────────────
    // Guest chat — no conversation ID, no DB persistence
    // Works for everyone; authenticated users can also use this.
    // ────────────────────────────────────────────────────────────

    @PostMapping("/chat")
    public ResponseEntity<AiMessageResponse> guestChat(
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(aiService.handleGuestChat(request, authentication));
    }
}
