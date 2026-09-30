package com.travelit.ai.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class SendMessageRequest {

    @NotBlank(message = "Message is required")
    private String content;

    private List<HistoryItem> history;

    private ClientContext clientContext;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryItem> history) {
        this.history = history;
    }

    public ClientContext getClientContext() {
        return clientContext;
    }

    public void setClientContext(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    public static class ClientContext {
        private String localTime;
        private String timeZone;
        private String timeOfDay;
        private String currentLocation;
        private Double latitude;
        private Double longitude;

        public ClientContext() {
        }

        public String getLocalTime() {
            return localTime;
        }

        public void setLocalTime(String localTime) {
            this.localTime = localTime;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getTimeOfDay() {
            return timeOfDay;
        }

        public void setTimeOfDay(String timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

        public String getCurrentLocation() {
            return currentLocation;
        }

        public void setCurrentLocation(String currentLocation) {
            this.currentLocation = currentLocation;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }

    public static class HistoryItem {
        private String role;
        private String content;

        public HistoryItem() {
        }

        public HistoryItem(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}