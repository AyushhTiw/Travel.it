package com.travelit.ai.provider;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class GroqAiProvider implements AiProvider {

    private static final String GROQ_API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final AiProperties aiProperties;
    private final RestTemplate restTemplate;

    public GroqAiProvider(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
        this.restTemplate = new RestTemplate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String generateResponse(String prompt) {

        try {

            System.out.println("======================================");
            System.out.println("GROQ REQUEST STARTED");
            System.out.println(
                    "Model: " + aiProperties.getGroq().getModel()
            );
            System.out.println("======================================");

            // =================================================
            // REQUEST HEADERS
            // =================================================

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.setBearerAuth(
                    aiProperties.getGroq().getApiKey()
            );

            // =================================================
            // REQUEST BODY
            // =================================================

            Map<String, Object> requestBody = Map.of(

                    "model",
                    aiProperties.getGroq().getModel(),

                    "messages",
                    List.of(
                            Map.of(
                                    "role",
                                    "user",

                                    "content",
                                    prompt
                            )
                    ),

                    "temperature",
                    0.7,

                    "max_tokens",
                    1500
            );

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(
                            requestBody,
                            headers
                    );

            // =================================================
            // GROQ API CALL WITH RETRY ON RATE LIMIT
            // =================================================

            ResponseEntity<Map> response = null;
            int maxAttempts = 4;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    response = restTemplate.exchange(
                            GROQ_API_URL,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );
                    break;
                } catch (org.springframework.web.client.HttpClientErrorException.TooManyRequests e) {
                    if (attempt == maxAttempts) {
                        throw e;
                    }
                    System.out.println("Groq rate limit (429) hit, retrying attempt " + attempt + " after wait...");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw e;
                    }
                }
            }



            // =================================================
            // RESPONSE BODY
            // =================================================

            Map<String, Object> responseBody =
                    response.getBody();

            if (responseBody == null) {

                throw new AiProviderException(
                        "Groq returned an empty response body"
                );
            }

            // =================================================
            // CHOICES
            // =================================================

            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>)
                            responseBody.get("choices");

            if (choices == null || choices.isEmpty()) {

                throw new AiProviderException(
                        "Groq response contains no choices"
                );
            }

            // =================================================
            // FIRST CHOICE
            // =================================================

            Map<String, Object> firstChoice =
                    choices.get(0);

            Map<String, Object> message =
                    (Map<String, Object>)
                            firstChoice.get("message");

            if (message == null) {

                throw new AiProviderException(
                        "Groq response contains no message"
                );
            }

            // =================================================
            // RESPONSE TEXT
            // =================================================

            String text =
                    (String) message.get("content");

            if (text == null || text.isBlank()) {

                throw new AiProviderException(
                        "Groq returned an empty response"
                );
            }

            System.out.println("======================================");
            System.out.println("GROQ RESPONSE:");
            System.out.println(text);
            System.out.println("======================================");

            return text;

        } catch (AiProviderException e) {

            throw e;

        } catch (Exception e) {

            System.err.println("======================================");
            System.err.println("GROQ API ERROR");
            System.err.println(
                    "Type: " + e.getClass().getName()
            );
            System.err.println(
                    "Message: " + e.getMessage()
            );
            System.err.println("======================================");

            throw new AiProviderException(
                    "Groq API request failed: "
                            + e.getMessage(),
                    e
            );
        }
    }
}
