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
import com.travelit.place.dto.NearbyPlaceForAi;
import com.travelit.place.service.PlaceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AiService {

    private static final int MAX_HISTORY = 15;

    private final AiConversationRepository conversationRepository;
    private final AiMessageRepository messageRepository;
    private final AiProvider aiProvider;
    private final AiPromptBuilder promptBuilder;
    private final AiGuardrail guardrail;
    private final PlaceService placeService;

    public AiService(
            AiConversationRepository conversationRepository,
            AiMessageRepository messageRepository,
            AiProvider aiProvider,
            AiPromptBuilder promptBuilder,
            AiGuardrail guardrail,
            PlaceService placeService
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.aiProvider = aiProvider;
        this.promptBuilder = promptBuilder;
        this.guardrail = guardrail;
        this.placeService = placeService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GUEST CHAT — no DB persistence, stateless
    // Called by POST /api/v1/ai/chat
    // Works for both guests (authentication == null) and logged-in users.
    // Authenticated users who want persistent history should use /conversations.
    // ─────────────────────────────────────────────────────────────────────────

    public AiMessageResponse handleGuestChat(
            SendMessageRequest request,
            Authentication authentication
    ) {
        String userMessage = request.getContent().trim();
        guardrail.validateUserMessage(userMessage);

        // Extract history passed from client if available (for guest context persistence)
        List<AiMessage> historyMessages = new java.util.ArrayList<>();
        if (request.getHistory() != null) {
            for (SendMessageRequest.HistoryItem item : request.getHistory()) {
                if (item.getRole() != null && item.getContent() != null) {
                    historyMessages.add(new AiMessage(0L, item.getRole().toUpperCase(), item.getContent()));
                }
            }
        }
        if (historyMessages.size() > MAX_HISTORY) {
            historyMessages = historyMessages.subList(historyMessages.size() - MAX_HISTORY, historyMessages.size());
        }

        boolean firstInteraction = historyMessages.isEmpty();

        // Check for nearby places discovery via Google Places API
        NearbyContext nearby = discoverNearbyPlacesIfRequested(userMessage, historyMessages, request.getClientContext());
        List<NearbyPlaceForAi> nearbyPlaces = nearby != null ? nearby.places() : null;
        String resolvedLocationName = nearby != null ? nearby.resolvedLocationName() : null;
        String detectedCategory = nearby != null ? nearby.category() : null;

        // Build prompt with conversation history context, real-time client context, and verified Google Places
        String prompt = promptBuilder.buildTravelPrompt(
                userMessage,
                historyMessages,
                firstInteraction,
                request.getClientContext(),
                nearbyPlaces,
                resolvedLocationName,
                detectedCategory
        );
        String aiResponse = aiProvider.generateResponse(prompt);
        aiResponse = cleanResponse(aiResponse, firstInteraction, userMessage);

        // Return a transient response — not saved to DB
        AiMessageResponse response = new AiMessageResponse();
        response.setId(System.currentTimeMillis());
        response.setConversationId(0L);
        response.setRole("ASSISTANT");
        response.setContent(aiResponse);
        response.setCreatedAt(LocalDateTime.now());
        return response;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // AUTHENTICATED — persistent conversation management
    // ─────────────────────────────────────────────────────────────────────────

    public AiConversationResponse createConversation(
            CreateConversationRequest request,
            Authentication authentication
    ) {
        User user = requireAuthenticatedUser(authentication);
        String title = request.getTitle().trim();
        AiConversation conversation = new AiConversation(user.getId(), title);
        return AiConversationResponse.from(conversationRepository.save(conversation));
    }

    public List<AiConversationResponse> getMyConversations(
            Authentication authentication
    ) {
        User user = requireAuthenticatedUser(authentication);
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
        AiConversation conversation = getConversationForUser(conversationId, authentication);
        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversation.getId())
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
        AiConversation conversation = getConversationForUser(conversationId, authentication);

        String userMessage = request.getContent().trim();
        guardrail.validateUserMessage(userMessage);

        // Fetch history BEFORE saving the current user message
        List<AiMessage> previousMessages =
                messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());

        boolean firstInteraction = previousMessages.isEmpty();

        // Save the user's message
        messageRepository.save(new AiMessage(conversation.getId(), "USER", userMessage));

        // Trim history to last MAX_HISTORY messages
        List<AiMessage> relevantHistory = previousMessages.size() > MAX_HISTORY
                ? previousMessages.subList(previousMessages.size() - MAX_HISTORY, previousMessages.size())
                : previousMessages;

        // Check for nearby places discovery via Google Places API
        NearbyContext nearby = discoverNearbyPlacesIfRequested(userMessage, relevantHistory, request.getClientContext());
        List<NearbyPlaceForAi> nearbyPlaces = nearby != null ? nearby.places() : null;
        String resolvedLocationName = nearby != null ? nearby.resolvedLocationName() : null;
        String detectedCategory = nearby != null ? nearby.category() : null;

        // Build prompt with full conversation context, real-time client context, and verified Google Places
        String prompt = promptBuilder.buildTravelPrompt(
                userMessage,
                relevantHistory,
                firstInteraction,
                request.getClientContext(),
                nearbyPlaces,
                resolvedLocationName,
                detectedCategory
        );
        String aiResponse = aiProvider.generateResponse(prompt);
        aiResponse = cleanResponse(aiResponse, firstInteraction, userMessage);

        // Save the assistant's response
        AiMessage savedMessage = messageRepository.save(
                new AiMessage(conversation.getId(), "ASSISTANT", aiResponse)
        );

        // Update conversation title on first message
        if (firstInteraction && ("Trevvy Chat".equalsIgnoreCase(conversation.getTitle())
                || conversation.getTitle().isBlank())) {
            String summary = userMessage.length() > 30
                    ? userMessage.substring(0, 30).trim() + "..."
                    : userMessage;
            conversation.setTitle(summary);
        }

        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return AiMessageResponse.from(savedMessage);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NEARBY PLACES DISCOVERY (GOOGLE PLACES API INTEGRATION)
    // ─────────────────────────────────────────────────────────────────────────

    private record NearbyContext(
            List<NearbyPlaceForAi> places,
            String resolvedLocationName,
            String category
    ) {}

    private NearbyContext discoverNearbyPlacesIfRequested(
            String userMessage,
            List<AiMessage> history,
            SendMessageRequest.ClientContext clientContext
    ) {
        if (!isNearbyQuery(userMessage)) {
            return null;
        }

        PlaceService.Coordinates coords = resolveCoordinates(userMessage, clientContext, history);
        if (coords == null) {
            System.out.println("[NearbyDiscovery] Nearby query detected, but no location coordinates resolved.");
            return new NearbyContext(null, null, "nearby");
        }

        String lower = userMessage.toLowerCase();
        List<NearbyPlaceForAi> places;
        String category = detectCategory(userMessage);

        // Check if the user is asking for a specific entity or item (e.g. toys, toilet, biryani, pharmacy, etc.)
        String specificQuery = extractSpecificItemQuery(lower);
        if (specificQuery != null) {
            System.out.println("[NearbyDiscovery] Text-biased query for '" + specificQuery + "' near "
                    + coords.formattedAddress() + " (" + coords.latitude() + ", " + coords.longitude() + ")");
            places = placeService.searchNearbyWithText(
                    specificQuery,
                    coords.latitude(),
                    coords.longitude(),
                    5000.0
            );
            category = specificQuery;
        } else {
            System.out.println("[NearbyDiscovery] Query for cat='" + category + "' near " + coords.formattedAddress()
                    + " (" + coords.latitude() + ", " + coords.longitude() + ")");
            places = placeService.searchNearbyForAi(
                    coords.latitude(),
                    coords.longitude(),
                    category,
                    5000.0
            );
        }

        return new NearbyContext(places, coords.formattedAddress(), category);
    }

    private String extractSpecificItemQuery(String lower) {
        if (lower.contains("toy")) return "toy shop";
        if (lower.contains("toilet") || lower.contains("restroom") || lower.contains("washroom") || lower.contains("loo")) {
            return "public toilet";
        }
        if (lower.contains("pharmacy") || lower.contains("chemist") || lower.contains("medical store")) {
            return "pharmacy";
        }
        if (lower.contains("atm") || lower.contains("cash")) {
            return "atm";
        }
        if (lower.contains("hospital")) {
            return "hospital";
        }
        if (lower.contains("petrol") || lower.contains("gas station")) {
            return "petrol pump";
        }
        if (lower.contains("gym") || lower.contains("fitness")) {
            return "gym";
        }
        if (lower.contains("biryani")) return "biryani restaurant";
        if (lower.contains("momo")) return "momos food";
        if (lower.contains("pizza")) return "pizza restaurant";
        if (lower.contains("coffee")) return "coffee cafe";
        return null;
    }

    private PlaceService.Coordinates resolveCoordinates(
            String userMessage,
            SendMessageRequest.ClientContext clientContext,
            List<AiMessage> history
    ) {
        // Priority 1: Current GPS location
        if (clientContext != null && clientContext.getLatitude() != null && clientContext.getLongitude() != null) {
            String name = clientContext.getCurrentLocation();
            if (name == null || name.isBlank()) name = "Current GPS Location";
            return new PlaceService.Coordinates(clientContext.getLatitude(), clientContext.getLongitude(), name);
        }

        // Priority 2: Explicit current location in clientContext
        if (clientContext != null && clientContext.getCurrentLocation() != null && !clientContext.getCurrentLocation().isBlank()) {
            PlaceService.Coordinates geocoded = placeService.geocodeLocation(clientContext.getCurrentLocation());
            if (geocoded != null) return geocoded;
        }

        // Priority 3: Explicit location stated in user message
        String extractedUserLoc = extractLocationFromText(userMessage);
        if (extractedUserLoc != null && !extractedUserLoc.isBlank()) {
            PlaceService.Coordinates geocoded = placeService.geocodeLocation(extractedUserLoc);
            if (geocoded != null) return geocoded;
        }

        // Priority 4: Active conversation destination / recent history
        if (history != null && !history.isEmpty()) {
            for (int i = history.size() - 1; i >= 0; i--) {
                AiMessage msg = history.get(i);
                String loc = extractLocationFromText(msg.getContent());
                if (loc != null && !loc.isBlank()) {
                    PlaceService.Coordinates geocoded = placeService.geocodeLocation(loc);
                    if (geocoded != null) return geocoded;
                }
            }
        }

        return null;
    }

    private String extractLocationFromText(String text) {
        if (text == null || text.isBlank()) return null;

        // Pattern 1: Explicit "User location: Mangolpuri, Delhi"
        var m1 = java.util.regex.Pattern.compile("(?i)user\\s+location:\\s*([^\\n\\r,]+(?:,\\s*[^\\n\\r]+)?)").matcher(text);
        if (m1.find()) {
            return m1.group(1).trim();
        }

        // Pattern 2: "Since you are in Mangolpuri" or "located in Mangolpuri" or "staying in Mangolpuri"
        var m2 = java.util.regex.Pattern.compile("(?i)(?:since you are in|as you are in|you are in|located in|staying in|standing in|based in)\\s+([A-Za-z0-9\\s-]+?)(?:\\s*\\(|\\s*,|\\s*\\.|\\s+area|\\s+district|\\s*$|\\s*\\n)").matcher(text);
        if (m2.find()) {
            String match = m2.group(1).trim();
            if (isValidLocalityName(match)) return match + ", Delhi";
        }

        // Pattern 3: "I am in Mangolpuri" / "I'm in Mangolpuri"
        var m3 = java.util.regex.Pattern.compile("(?i)(?:i am|i'm)\\s+(?:in|at)\\s+([A-Za-z0-9\\s-]+?)(?:\\s*\\(|\\s*,|\\s*\\.|\\s+area|\\s*$|\\s*\\n)").matcher(text);
        if (m3.find()) {
            String match = m3.group(1).trim();
            if (isValidLocalityName(match)) return match + ", Delhi";
        }

        // Pattern 4: "near Mangolpuri" / "around Mangolpuri"
        var m4 = java.util.regex.Pattern.compile("(?i)(?:near|around|closest to)\\s+([A-Za-z0-9\\s-]+?)(?:\\s*\\?|\\s*$|\\s*\\.|\\s*\\n)").matcher(text);
        if (m4.find()) {
            String match = m4.group(1).trim();
            if (!match.equalsIgnoreCase("me") && !match.equalsIgnoreCase("here") && isValidLocalityName(match)) {
                return match + ", Delhi";
            }
        }

        // Pattern 5: Direct mention of major Delhi localities
        String lower = text.toLowerCase();
        for (String loc : KNOWN_LOCALITIES) {
            if (lower.contains(loc.toLowerCase())) {
                return loc + ", Delhi";
            }
        }

        return null;
    }

    private static final List<String> KNOWN_LOCALITIES = List.of(
        "Mangolpuri", "Rohini", "Pitampura", "Sultanpuri", "Nangloi", "Paschim Vihar",
        "Janakpuri", "Dwarka", "Punjabi Bagh", "Karol Bagh", "Connaught Place",
        "Rajiv Chowk", "Chandni Chowk", "Hauz Khas", "Vasant Kunj", "Saket",
        "Lajpat Nagar", "India Gate", "Greater Kailash", "Daryaganj", "Paharganj",
        "Noida", "Gurgaon"
    );

    private boolean isValidLocalityName(String s) {
        if (s == null || s.isBlank() || s.length() < 3 || s.length() > 40) return false;
        String lower = s.toLowerCase();
        return !lower.equals("a") && !lower.equals("the") && !lower.equals("this")
                && !lower.equals("trip") && !lower.equals("hotel") && !lower.equals("delhi");
    }

    private boolean isNearbyQuery(String message) {
        if (message == null || message.isBlank()) return false;
        String lower = message.toLowerCase().trim();
        return lower.matches(".*\\b(?:ne+a+r+|around|close\\s*to)\\s*(?:me|here)?\\b.*")
                || lower.matches(".*\\b(?:ne+a+r+by|close\\s*by|in\\s+my\\s+area)\\b.*")
                || lower.matches(".*\\b(?:adventure|food|restaurants?|cafes?|hotels?|shopping|shops?|stores?|places|spots|toilets?|restrooms?|washrooms?|toys?)\\s+(?:ne+a+r+|around).*")
                || lower.matches(".*\\b(?:ne+a+r+|around)\\s+(?:me|here)?\\s*(?:for\\s+)?(?:toilets?|restrooms?|washrooms?|toys?|shops?|stores?|food|restaurants?).*")
                || lower.matches(".*\\bplaces?\\s+(?:to\\s+visit\\s+)?(?:ne+a+r+|around).*");
    }

    private String detectCategory(String message) {
        if (message == null) return "best_spots";
        String lower = message.toLowerCase();
        if (lower.contains("adventure") || lower.contains("amusement") || lower.contains("theme park")
                || lower.contains("water park") || lower.contains("thrill") || lower.contains("ride")
                || lower.contains("trampoline") || lower.contains("kart") || lower.contains("go-kart")
                || lower.contains("climbing") || lower.contains("bouldering") || lower.contains("fun park")
                || lower.contains("game zone") || lower.contains("games") || lower.contains("arcade")) {
            return "adventure";
        }
        if (lower.contains("food") || lower.contains("restaurant") || lower.contains("cafe")
                || lower.contains("dinner") || lower.contains("lunch") || lower.contains("breakfast")
                || lower.contains("eat") || lower.contains("street food") || lower.contains("snack")
                || lower.contains("bakery") || lower.contains("momo") || lower.contains("biryani")
                || lower.contains("chaat")) {
            return "food";
        }
        if (lower.contains("shopping") || lower.contains("mall") || lower.contains("market")
                || lower.contains("bazaar") || lower.contains("shop") || lower.contains("clothes")
                || lower.contains("buy")) {
            return "shopping";
        }
        if (lower.contains("hotel") || lower.contains("stay") || lower.contains("resort")
                || lower.contains("lodge") || lower.contains("hostel") || lower.contains("accommodation")
                || lower.contains("room")) {
            return "hotels";
        }
        if (lower.contains("tourist") || lower.contains("attraction") || lower.contains("sightseeing")
                || lower.contains("monument") || lower.contains("heritage") || lower.contains("visit")
                || lower.contains("museum")) {
            return "tourist_spots";
        }
        return "best_spots";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private String cleanResponse(String response, boolean firstInteraction, String userMessage) {
        if (response == null || response.isBlank()) return response;
        String cleaned = response.trim();

        // Strip unwanted re-introductions
        cleaned = cleaned.replaceAll(
                "(?i)^(?:(?:Hey(?:\\s+there)?|Hello|Hi|Greetings)[!.,]?\\s*)?(?:I'm|I am|This is)\\s+(?:Trevvy|your\\s+(?:AI\\s+)?(?:travel\\s+)?assistant[^.!\n]*)[.!?\n]+\\s*", "");
        cleaned = cleaned.replaceAll(
                "(?i)^(?:(?:Hey(?:\\s+there)?|Hello|Hi)[!.,]?\\s*)?I'm\\s+Trevvy[^.!?\n]*[.!?\n]+\\s*", "");
        cleaned = cleaned.replaceAll("(?i)^As\\s+Trevvy[^.!?\n]*[.!?\n]+\\s*", "");
        cleaned = cleaned.replaceAll(
                "(?i)^(?:(?:Hey(?:\\s+there)?|Hello|Hi)[!.,]?\\s*)?Where\\s+would\\s+you\\s+like\\s+to\\s+go[^.!?\n]*[.!?\n]+\\s*", "");

        // Remove orphaned horizontal rules
        cleaned = cleaned.replaceAll("(?m)^\\s*---\\s*$", "").trim();

        // Enforce INR unless user explicitly asked for another currency
        if (userMessage == null
                || (!userMessage.contains("$")
                && !userMessage.toLowerCase().contains("usd")
                && !userMessage.toLowerCase().contains("dollar"))) {
            cleaned = cleaned.replaceAll("(?i)\\b(?:US\\$|USD)\\s*([0-9,]+(?:\\.[0-9]+)?)", "₹$1");
            cleaned = cleaned.replaceAll("\\$\\s*([0-9,]+(?:\\.[0-9]+)?)", "₹$1");
        }

        return cleaned.isBlank() ? response.trim() : cleaned;
    }

    private User requireAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof User user)) {
            throw new IllegalArgumentException("Authenticated user not found");
        }
        return (User) authentication.getPrincipal();
    }

    private AiConversation getConversationForUser(
            Long conversationId, Authentication authentication) {
        User user = requireAuthenticatedUser(authentication);
        return conversationRepository
                .findById(conversationId)
                .filter(c -> c.getUserId().equals(user.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));
    }
}
