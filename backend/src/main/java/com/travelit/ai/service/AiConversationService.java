package com.travelit.ai.service;

import com.travelit.ai.dto.AiConversationDetailResponse;
import com.travelit.ai.dto.AiConversationResponse;
import com.travelit.ai.dto.AiMessageResponse;
import com.travelit.ai.entity.AiConversation;
import com.travelit.ai.repository.AiConversationRepository;
import com.travelit.ai.repository.AiMessageRepository;
import com.travelit.auth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AiConversationService {

    private final AiConversationRepository conversationRepository;
    private final AiMessageRepository messageRepository;

    public AiConversationService(
            AiConversationRepository conversationRepository,
            AiMessageRepository messageRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public AiConversationDetailResponse getConversation(
            Long conversationId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        AiConversation conversation =
                conversationRepository.findById(conversationId)
                        .filter(item ->
                                item.getUserId().equals(user.getId())
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Conversation not found"
                                )
                        );

        return new AiConversationDetailResponse(
                AiConversationResponse.from(conversation),
                messageRepository
                        .findByConversationIdOrderByCreatedAtAsc(
                                conversation.getId()
                        )
                        .stream()
                        .map(AiMessageResponse::from)
                        .toList()
        );
    }

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        if (authentication == null
                || !(authentication.getPrincipal()
                instanceof User user)) {

            throw new IllegalArgumentException(
                    "Authenticated user not found"
            );
        }

        return user;
    }
}