package com.travelit.ai.dto;

import java.util.List;

public class AiConversationDetailResponse {

    private AiConversationResponse conversation;
    private List<AiMessageResponse> messages;

    public AiConversationDetailResponse() {
    }

    public AiConversationDetailResponse(
            AiConversationResponse conversation,
            List<AiMessageResponse> messages
    ) {
        this.conversation = conversation;
        this.messages = messages;
    }

    public AiConversationResponse getConversation() {
        return conversation;
    }

    public List<AiMessageResponse> getMessages() {
        return messages;
    }
}