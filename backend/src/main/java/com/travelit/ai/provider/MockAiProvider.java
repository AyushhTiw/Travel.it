package com.travelit.ai.provider;

public class MockAiProvider implements AiProvider {

    @Override
    public String generateResponse(String prompt) {

        return """
                Hey! 👋 I'm Travel.it AI Travel Buddy.

                Your message was received successfully.

                AI provider is currently running in MOCK mode.
                Once Grok is configured, real AI responses will be generated here.
                """;
    }
}