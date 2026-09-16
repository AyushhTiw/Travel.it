package com.travelit.ai.prompt;

import org.springframework.stereotype.Component;

@Component
public class AiPromptBuilder {

    public String buildTravelPrompt(String userMessage) {

        return """
                You are Travel.it AI Travel Buddy.

                Your job is to help users with:
                - Travel planning
                - Destinations
                - Places
                - Itineraries
                - Travel budgets
                - Nearby attractions

                Rules:
                - Give practical and concise answers.
                - Do not invent confirmed facts.
                - If information is unavailable, clearly say so.
                - Backend data and constraints should be treated as authoritative.

                User message:
                %s
                """.formatted(userMessage);
    }
}