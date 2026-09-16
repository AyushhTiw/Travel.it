package com.travelit.ai.dto;

public class AiHealthResponse {

    private String provider;
    private String status;

    public AiHealthResponse() {
    }

    public AiHealthResponse(
            String provider,
            String status
    ) {
        this.provider = provider;
        this.status = status;
    }

    public String getProvider() {
        return provider;
    }

    public String getStatus() {
        return status;
    }
}