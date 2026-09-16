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

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/conversations")
    public ResponseEntity<AiConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                aiService.createConversation(
                        request,
                        authentication
                )
        );
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<AiConversationResponse>>
    getMyConversations(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                aiService.getMyConversations(authentication)
        );
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<AiMessageResponse>> getMessages(
            @PathVariable Long conversationId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                aiService.getMessages(
                        conversationId,
                        authentication
                )
        );
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<AiMessageResponse> sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody SendMessageRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                aiService.sendMessage(
                        conversationId,
                        request,
                        authentication
                )
        );
    }
}