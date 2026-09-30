package com.travelit.ai.provider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfiguration {

    @Bean
    public AiProvider aiProvider(AiProperties properties) {

        String provider = properties.getProvider();

        if ("gemini".equalsIgnoreCase(provider)) {
            return new GeminiAiProvider(properties);
        }

        if ("groq".equalsIgnoreCase(provider)) {
            return new GroqAiProvider(properties);
        }

        if ("mock".equalsIgnoreCase(provider)) {
            return new MockAiProvider();
        }

        throw new IllegalStateException(
                "Unknown AI provider: " + provider + ". Valid options: gemini, groq, mock"
        );
    }
}
