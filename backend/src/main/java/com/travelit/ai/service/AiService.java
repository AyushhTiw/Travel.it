package com.travelit.ai.service;

import com.travelit.ai.dto.AiConversationResponse;
import com.travelit.ai.dto.AiMessageResponse;
import com.travelit.ai.dto.CreateConversationRequest;
import com.travelit.ai.dto.SendMessageRequest;
import com.travelit.ai.entity.AiConversation;
import com.travelit.ai.entity.AiMessage;
import com.travelit.ai.guardrail.AiGuardrail;
import com.travelit.ai.prompt.AiPromptBuilder;
import com.travelit.ai.provider.AiProvider;
import com.travelit.ai.repository.AiConversationRepository;
import com.travelit.ai.repository.AiMessageRepository;
import com.travelit.auth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AiService {

    private final AiConversationRepository conversationRepository;
    private final AiMessageRepository messageRepository;
    private final AiProvider aiProvider;
    private final AiPromptBuilder promptBuilder;
    private final AiGuardrail guardrail;

    public AiService(
            AiConversationRepository conversationRepository,
            AiMessageRepository messageRepository,
            AiProvider aiProvider,
            AiPromptBuilder promptBuilder,
            AiGuardrail guardrail
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.aiProvider = aiProvider;
        this.promptBuilder = promptBuilder;
        this.guardrail = guardrail;
    }

    public AiConversationResponse createConversation(
            CreateConversationRequest request,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        String title = request.getTitle().trim();

        AiConversation conversation =
                new AiConversation(user.getId(), title);

        return AiConversationResponse.from(
                conversationRepository.save(conversation)
        );
    }

    public List<AiConversationResponse> getMyConversations(
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return conversationRepository
                .findByUserIdOrderByUpdatedAtDesc(user.getId())
                .stream()
                .map(AiConversationResponse::from)
                .toList();
    }

    public List<AiMessageResponse> getMessages(
            Long conversationId,
            Authentication authentication
    ) {

        AiConversation conversation =
                getConversationForUser(
                        conversationId,
                        authentication
                );

        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(
                        conversation.getId()
                )
                .stream()
                .map(AiMessageResponse::from)
                .toList();
    }

    @Transactional
    public AiMessageResponse sendMessage(
            Long conversationId,
            SendMessageRequest request,
            Authentication authentication
    ) {

        AiConversation conversation =
                getConversationForUser(
                        conversationId,
                        authentication
                );

        String userMessage = request.getContent().trim();

        guardrail.validateUserMessage(userMessage);

        AiMessage userMessageEntity =
                new AiMessage(
                        conversation.getId(),
                        "USER",
                        userMessage
                );

        messageRepository.save(userMessageEntity);

        String prompt =
                promptBuilder.buildTravelPrompt(userMessage);

        String aiResponse =
                aiProvider.generateResponse(prompt);

        AiMessage assistantMessage =
                new AiMessage(
                        conversation.getId(),
                        "ASSISTANT",
                        aiResponse
                );

        AiMessage savedMessage =
                messageRepository.save(assistantMessage);

        conversation.setUpdatedAt(
                java.time.LocalDateTime.now()
        );

        conversationRepository.save(conversation);

        return AiMessageResponse.from(savedMessage);
    }

    private User getAuthenticatedUser(
            Authentication authentication
    ) {

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User user)) {

            throw new IllegalArgumentException(
                    "Authenticated user not found"
            );
        }

        return user;
    }

    private AiConversation getConversationForUser(
            Long conversationId,
            Authentication authentication
    ) {

        User user = getAuthenticatedUser(authentication);

        return conversationRepository
                .findById(conversationId)
                .filter(conversation ->
                        conversation.getUserId()
                                .equals(user.getId())
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Conversation not found"
                        )
                );
    }
}