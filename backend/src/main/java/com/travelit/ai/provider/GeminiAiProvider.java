package com.travelit.ai.provider;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiAiProvider implements AiProvider {

    private final Client client;
    private final AiProperties aiProperties;

    public GeminiAiProvider(AiProperties aiProperties) {
        this.aiProperties = aiProperties;

        this.client = Client.builder()
                .apiKey(aiProperties.getGemini().getApiKey())
                .build();
    }

    @Override
    public String generateResponse(String prompt) {

        try {

            System.out.println("======================================");
            System.out.println("GEMINI REQUEST STARTED");
            System.out.println("Model: " + aiProperties.getGemini().getModel());
            System.out.println("Prompt: " + prompt);
            System.out.println("======================================");

            GenerateContentResponse response =
                    client.models.generateContent(
                            aiProperties.getGemini().getModel(),
                            prompt,
                            null
                    );

            String text = response.text();

            System.out.println("======================================");
            System.out.println("GEMINI RESPONSE:");
            System.out.println(text);
            System.out.println("======================================");

            if (text == null || text.isBlank()) {
                throw new AiProviderException(
                        "Gemini returned an empty response"
                );
            }

            return text;

        } catch (Exception e) {

            System.err.println("======================================");
            System.err.println("GEMINI API ERROR");
            System.err.println("Type: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.err.println("======================================");

            throw new AiProviderException(
                    "Gemini API request failed: " + e.getMessage(), e
            );
        }
    }
}