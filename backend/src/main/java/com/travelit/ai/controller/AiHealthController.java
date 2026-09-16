package com.travelit.ai.controller;

import com.travelit.ai.dto.AiHealthResponse;
import com.travelit.ai.provider.AiProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiHealthController {

    private final AiProperties aiProperties;

    public AiHealthController(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
    }

    @GetMapping("/health")
    public ResponseEntity<AiHealthResponse> health() {

        return ResponseEntity.ok(
                new AiHealthResponse(
                        aiProperties.getProvider(),
                        "UP"
                )
        );
    }
}