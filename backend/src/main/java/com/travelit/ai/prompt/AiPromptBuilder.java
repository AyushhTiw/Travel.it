package com.travelit.ai.prompt;

import com.travelit.ai.dto.SendMessageRequest;
import com.travelit.ai.entity.AiMessage;
import com.travelit.place.dto.NearbyPlaceForAi;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AiPromptBuilder {

    /**
     * Legacy single-arg overload — used when no history or client context is passed.
     */
    public String buildTravelPrompt(String userMessage) {
        return buildTravelPrompt(userMessage, List.of(), true, null, null, null, null);
    }

    /**
     * Overload with conversation history.
     */
    public String buildTravelPrompt(
            String userMessage,
            List<AiMessage> history,
            boolean firstInteraction
    ) {
        return buildTravelPrompt(userMessage, history, firstInteraction, null, null, null, null);
    }

    /**
     * Overload with conversation history and client context.
     */
    public String buildTravelPrompt(
            String userMessage,
            List<AiMessage> history,
            boolean firstInteraction,
            SendMessageRequest.ClientContext clientContext
    ) {
        return buildTravelPrompt(userMessage, history, firstInteraction, clientContext, null, null, null);
    }

    /**
     * Full prompt builder with conversation history, client context, and verified real Google Places.
     *
     * @param userMessage          the current user message
     * @param history              previous messages in this conversation (oldest first)
     * @param firstInteraction     true if this is the very first message in the conversation
     * @param clientContext        real-time client context (local time, day of week, location)
     * @param nearbyPlaces         verified places returned from Google Places API (or null)
     * @param resolvedLocationName location name used for nearby search (e.g. "Mangolpuri, Delhi")
     * @param nearbyCategory       detected nearby search category (e.g. "adventure", "food")
     */
    public String buildTravelPrompt(
            String userMessage,
            List<AiMessage> history,
            boolean firstInteraction,
            SendMessageRequest.ClientContext clientContext,
            List<NearbyPlaceForAi> nearbyPlaces,
            String resolvedLocationName,
            String nearbyCategory
    ) {
        StringBuilder sb = new StringBuilder();

        // ── IDENTITY ──────────────────────────────────────────────────────────
        sb.append("You are Trevvy, the expert real-time AI travel assistant for Travel.it.\n\n");

        // ── NO RE-INTRODUCTIONS ───────────────────────────────────────────────
        sb.append("CRITICAL RULE: DO NOT REINTRODUCE YOURSELF.\n");
        sb.append("- NEVER start responses with 'Hi, I'm Trevvy', 'Hey, I'm Trevvy', 'I'm your AI travel assistant', or any greeting/welcome message.\n");
        sb.append("- Jump DIRECTLY into answering the user's request.\n\n");

        // ── REAL VERIFIED NEARBY PLACES FROM GOOGLE PLACES API ────────────────
        if (nearbyPlaces != null && !nearbyPlaces.isEmpty()) {
            sb.append("REAL VERIFIED NEARBY PLACES (FROM GOOGLE PLACES API - STRICT):\n");
            sb.append("- Location: ").append(resolvedLocationName != null ? resolvedLocationName : "User's current location").append("\n");
            sb.append("- Category: ").append(nearbyCategory != null ? nearbyCategory.toUpperCase() : "NEARBY").append("\n");
            sb.append("- MANDATORY: You MUST base your recommendations ONLY on these real verified places discovered via Google Places API.\n");
            sb.append("- DO NOT invent, hallucinate, or recommend random or distant venues (e.g., South Delhi Ridge, Vasant Kunj, Kashmere Gate) when real options exist right nearby in this list.\n");
            sb.append("- NEVER say 'there are no adventure options nearby' or 'you are far from adventure' when real options like amusement parks or adventure centers are in this list.\n");
            sb.append("- For each recommended place, provide the real name, exact distance (e.g. '~4.0 km away'), rating, and address.\n");
            sb.append("- Explain why each fits the user's intent (e.g., Adventure Island is an expansive amusement park with rides, water splash, and entertainment right opposite Rithala Metro Station in Sector 10 Rohini; SkyJumper Trampoline Park provides indoor high-adrenaline jumping & climbing).\n");
            sb.append("- Do NOT fabricate ticket prices, opening hours, or distant travel times—use only verified facts or clearly state to check current rates at the ticket counter.\n\n");
            sb.append("DISCOVERED PLACES FROM GOOGLE PLACES API:\n");
            for (int i = 0; i < nearbyPlaces.size(); i++) {
                NearbyPlaceForAi p = nearbyPlaces.get(i);
                sb.append(i + 1).append(". **").append(p.getName()).append("**\n");
                if (p.getPrimaryType() != null) sb.append("   • Type: ").append(p.getPrimaryType().replace("_", " ")).append("\n");
                if (p.getAddress() != null) sb.append("   • Address: ").append(p.getAddress()).append("\n");
                if (p.getDistanceKm() != null) sb.append("   • Distance: ~").append(p.getDistanceKm()).append(" km away\n");
                if (p.getRating() != null) sb.append("   • Rating: ").append(p.getRating()).append(" / 5.0\n");
                if (p.getGoogleMapsUrl() != null) sb.append("   • Map: ").append(p.getGoogleMapsUrl()).append("\n");
                sb.append("\n");
            }
        } else if (nearbyCategory != null && resolvedLocationName != null) {
            sb.append("NEARBY SEARCH RESULT:\n");
            sb.append("- Google Places API returned 0 matching verified places for category '").append(nearbyCategory).append("' within the search radius of ").append(resolvedLocationName).append(".\n");
            sb.append("- Inform the user honestly that no verified ").append(nearbyCategory).append(" venues were found right within the immediate radius of ").append(resolvedLocationName).append(". Suggest expanding the search radius or heading to the nearest major hub.\n\n");
        }

        // ── REAL-TIME USAGE & SITUATIONAL AWARENESS ───────────────────────────
        sb.append("REAL-TIME TRAVEL AWARENESS:\n");
        if (clientContext != null) {
            if (clientContext.getLocalTime() != null && !clientContext.getLocalTime().isBlank()) {
                sb.append("- Current User Local Time: ").append(clientContext.getLocalTime()).append("\n");
            }
            if (clientContext.getTimeOfDay() != null && !clientContext.getTimeOfDay().isBlank()) {
                sb.append("- Time of Day: ").append(clientContext.getTimeOfDay()).append("\n");
            }
            if (clientContext.getCurrentLocation() != null && !clientContext.getCurrentLocation().isBlank()) {
                sb.append("- User's Current Location: ").append(clientContext.getCurrentLocation()).append("\n");
            }
        }
        sb.append("- Be time-aware in real-time: factor in what is open RIGHT NOW (e.g. morning breakfast vs lunch vs evening snacks vs dinner; monuments closing at sunset; weekly market closure days like Sarojini Nagar on Mondays, Lotus Temple & Red Fort museums on Mondays).\n");
        sb.append("- Respond dynamically as a living, real-time travel companion.\n\n");

        // ── NO ARBITRARY NEIGHBORHOOD BIAS (CRITICAL) ─────────────────────────
        sb.append("NEVER BIAS TO A SPECIFIC SUB-NEIGHBORHOOD (CRITICAL):\n");
        sb.append("- The active destination is the overall city/region (e.g. Delhi).\n");
        sb.append("- DO NOT assume the user is staying in or located at Connaught Place (CP), Rajiv Chowk, or any specific locality unless the user EXPLICITLY said they are there!\n");
        sb.append("- When the user asks 'NEAR ME', 'FOOD NEAR ME', or 'WHERE TO GO NEARBY':\n");
        sb.append("  • If the user's specific neighborhood is unknown, organize recommendations across Delhi's major zones (e.g. Old Delhi, Central Delhi, South Delhi, North Delhi) and ask: 'Which area or metro station in Delhi are you currently closest to?'\n");
        sb.append("  • NEVER say 'Since you are in the CP/Rajiv Chowk area' unless the user previously stated that is where they are staying or standing!\n\n");

        // ── CONTEXT MEMORY & TOPIC CONTINUITY ──────────────────────────────────
        sb.append("CONTEXT & TOPIC CONTINUITY:\n");
        sb.append("- Treat this chat as ONE continuous conversation.\n");
        sb.append("- Retain active city context (e.g. Delhi) for follow-up requests without asking 'What city are you visiting?'.\n");
        sb.append("- Handle conversational shorthand intelligently in context:\n");
        sb.append("  • In a food context, 'SF' means 'Street Food'.\n");
        sb.append("  • Single digits ('1', '2', '3') select the corresponding option from the previous message.\n");
        sb.append("  • If the user explicitly asks about 'cp' or 'canaut place', resolve it to Connaught Place; if they mention 'rajiv chowk', resolve to Rajiv Chowk Delhi.\n\n");

        // ── SMART TRAVEL EXPERTISE & REALISTIC BUDGETS ────────────────────────
        sb.append("SMART TRAVEL EXPERTISE & REALISTIC BUDGETS:\n");
        sb.append("- Be practical, highly knowledgeable, and user-oriented. Act like a savvy local insider.\n");
        sb.append("- When budgets are tight (e.g. ₹1,000 for 2 people), suggest genuine affordable local hacks (Delhi Metro smart card, iconic free spots, authentic affordable street food) instead of expensive luxury restaurants.\n");
        sb.append("- Keep responses complete, well-structured, and easy to read on mobile or desktop.\n\n");

        // ── CONFIRMATION HANDLING ─────────────────────────────────────────────
        sb.append("CONFIRMATION RULE (CRITICAL):\n");
        sb.append("If the user says: yes / yeah / ok / okay / sure / haan / ha / yep / go ahead / do it / continue / theek hai / bilkul\n");
        sb.append("→ Look at the IMMEDIATELY PRECEDING assistant message in history. Fulfill that offer DIRECTLY without asking 'What would you like to do?'.\n\n");

        // ── CURRENCY ─────────────────────────────────────────────────────────
        sb.append("CURRENCY RULE (STRICT):\n");
        sb.append("- Always use ₹ (Indian Rupees) for ALL prices and budgets unless another currency is explicitly requested.\n\n");

        // ── FORMATTING ────────────────────────────────────────────────────────
        sb.append("FORMATTING RULES:\n");
        sb.append("- Use clean Markdown: ## headings, **bold**, - bullet lists, blank lines between sections.\n");
        sb.append("- Do NOT output raw symbols like ###, ***, ---, or __ as visible text.\n");
        sb.append("- Do NOT use --- as a horizontal separator.\n");
        sb.append("- Keep responses concise, high-value, and easy to scan.\n\n");

        // ── CONVERSATION HISTORY ─────────────────────────────────────────────
        if (history != null && !history.isEmpty()) {
            sb.append("## Conversation History (Oldest to Newest)\n\n");
            for (AiMessage msg : history) {
                String role = "ASSISTANT".equalsIgnoreCase(msg.getRole()) ? "Trevvy" : "User";
                sb.append("**").append(role).append(":** ")
                  .append(msg.getContent().trim())
                  .append("\n\n");
            }
        }

        // ── CURRENT USER MESSAGE ─────────────────────────────────────────────
        sb.append("## User's Current Message\n\n");
        sb.append(userMessage.trim()).append("\n\n");
        sb.append("**Trevvy's response (answering directly with zero introduction):**");

        return sb.toString();
    }
}
