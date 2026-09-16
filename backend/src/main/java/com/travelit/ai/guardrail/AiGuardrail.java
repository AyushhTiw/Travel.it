package com.travelit.ai.guardrail;

import org.springframework.stereotype.Component;

@Component
public class AiGuardrail {

    public void validateUserMessage(String message) {

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty"
            );
        }

        if (message.length() > 5000) {
            throw new IllegalArgumentException(
                    "Message is too long"
            );
        }
    }
}