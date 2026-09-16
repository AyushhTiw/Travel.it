package com.travelit.ai.controller;

import com.travelit.ai.dto.AiConversationDetailResponse;
import com.travelit.ai.service.AiConversationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai/conversations")
public class AiConversationController {

    private final AiConversationService aiConversationService;

    public AiConversationController(
            AiConversationService aiConversationService
    ) {
        this.aiConversationService = aiConversationService;
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<AiConversationDetailResponse>
    getConversation(
            @PathVariable Long conversationId,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                aiConversationService.getConversation(
                        conversationId,
                        authentication
                )
        );
    }
}