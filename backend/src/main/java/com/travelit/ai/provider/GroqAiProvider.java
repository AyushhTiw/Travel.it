package com.travelit.ai.provider;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@ConditionalOnProperty(
        name = "ai.provider",
        havingValue = "groq"
)
public class GroqAiProvider implements AiProvider {

    private final AiProperties aiProperties;
    private final RestClient restClient;

    public GroqAiProvider(AiProperties aiProperties) {

        this.aiProperties = aiProperties;

        this.restClient = RestClient.builder()
                .baseUrl(aiProperties.getBaseUrl())
                .defaultHeader(
                        "Authorization",
                        "Bearer " + aiProperties.getApiKey()
                )
                .defaultHeader(
                        "Content-Type",
                        "application/json"
                )
                .build();
    }

    @Override
    public String generateResponse(String prompt) {

        if (aiProperties.getApiKey() == null
                || aiProperties.getApiKey().isBlank()) {

            throw new AiProviderException(
                    "Groq API key is not configured"
            );
        }

        GroqRequest request = new GroqRequest(
                aiProperties.getModel(),
                List.of(
                        new GroqMessage(
                                "user",
                                prompt
                        )
                )
        );

        try {

            GroqResponse response = restClient
                    .post()
                    .uri("/chat/completions")
                    .body(request)
                    .retrieve()
                    .body(GroqResponse.class);

            if (response == null
                    || response.choices() == null
                    || response.choices().isEmpty()
                    || response.choices().get(0).message() == null
                    || response.choices().get(0).message().content() == null) {

                throw new AiProviderException(
                        "Invalid response received from Groq"
                );
            }

            return response.choices()
                    .get(0)
                    .message()
                    .content();

        } catch (AiProviderException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new AiProviderException(
                    "Groq API request failed",
                    exception
            );
        }
    }

    private record GroqRequest(
            String model,
            List<GroqMessage> messages
    ) {
    }

    private record GroqMessage(
            String role,
            String content
    ) {
    }

    private record GroqResponse(
            List<GroqChoice> choices
    ) {
    }

    private record GroqChoice(
            GroqMessage message
    ) {
    }
}