package com.travelit.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelit.ai.dto.AiMessageResponse;
import com.travelit.ai.dto.SendMessageRequest;
import com.travelit.ai.guardrail.AiGuardrail;
import com.travelit.ai.prompt.AiPromptBuilder;
import com.travelit.ai.provider.AiProvider;
import com.travelit.ai.repository.AiConversationRepository;
import com.travelit.ai.repository.AiMessageRepository;
import com.travelit.ai.service.AiService;
import com.travelit.place.dto.NearbyPlaceForAi;
import com.travelit.place.repository.PlaceRepository;
import com.travelit.place.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiNearbyPlacesTest {

    @Mock
    private AiConversationRepository conversationRepository;

    @Mock
    private AiMessageRepository messageRepository;

    @Mock
    private AiProvider aiProvider;

    @Mock
    private PlaceRepository placeRepository;

    private PlaceService placeService;
    private AiPromptBuilder promptBuilder;
    private AiGuardrail guardrail;
    private AiService aiService;

    @BeforeEach
    void setUp() {
        promptBuilder = new AiPromptBuilder();
        guardrail = new AiGuardrail();
        placeService = spy(new PlaceService(placeRepository, new ObjectMapper()));
        ReflectionTestUtils.setField(placeService, "googleMapsApiKey", "dummy-key");

        aiService = new AiService(
                conversationRepository,
                messageRepository,
                aiProvider,
                promptBuilder,
                guardrail,
                placeService
        );
    }

    @Test
    void testAdventureNearMe_ResolvesMangolpuriAndProvidesAdventureIsland() {
        // Mock geocode of Mangolpuri, Delhi
        doReturn(new PlaceService.Coordinates(28.6923809, 77.0916899, "Mangolpuri, Delhi, India"))
                .when(placeService).geocodeLocation(anyString());

        // Mock searchNearbyForAi returning Adventure Island & SkyJumper Trampoline Park
        NearbyPlaceForAi adventureIsland = new NearbyPlaceForAi(
                "place_adv_island",
                "Adventure Island",
                "opposite Rithala Metro Station, Swarn Jayanti Park, Sector 10, Rohini, Delhi, 110085, India",
                28.7237,
                77.1124,
                4.0,
                4.0,
                "amusement_park",
                "https://maps.google.com/?cid=3042736891425790480"
        );
        NearbyPlaceForAi skyJumper = new NearbyPlaceForAi(
                "place_skyjumper",
                "SkyJumper Trampoline and Amusement Park",
                "Ground Floor, inside Adventure Island Metro Walk, Sector 10, Rohini, Delhi",
                28.7248,
                77.1157,
                4.3,
                4.9,
                "amusement_park",
                "https://maps.google.com/?cid=17781770527766633103"
        );

        doReturn(List.of(adventureIsland, skyJumper))
                .when(placeService).searchNearbyForAi(eq(28.6923809), eq(77.0916899), eq("adventure"), anyDouble());

        when(aiProvider.generateResponse(anyString())).thenReturn(
                "Adventure Island in Sector 10, Rohini is your top adventure destination, located just ~4.0 km away opposite Rithala Metro Station."
        );

        SendMessageRequest request = new SendMessageRequest();
        request.setContent("ADVENTURE NEAR ME");

        SendMessageRequest.ClientContext context = new SendMessageRequest.ClientContext();
        context.setCurrentLocation("Mangolpuri, Delhi");
        request.setClientContext(context);

        AiMessageResponse response = aiService.handleGuestChat(request, null);

        assertNotNull(response);
        assertNotNull(response.getContent());
        assertTrue(response.getContent().contains("Adventure Island"));

        // Verify the prompt that was sent to the AI Provider
        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(aiProvider).generateResponse(promptCaptor.capture());
        String generatedPrompt = promptCaptor.getValue();

        // Must contain real verified places from Google Places API
        assertTrue(generatedPrompt.contains("REAL VERIFIED NEARBY PLACES (FROM GOOGLE PLACES API - STRICT)"));
        assertTrue(generatedPrompt.contains("Adventure Island"));
        assertTrue(generatedPrompt.contains("Sector 10, Rohini"));
        assertTrue(generatedPrompt.contains("4.0 km away"));
        assertTrue(generatedPrompt.contains("SkyJumper Trampoline and Amusement Park"));
        assertTrue(generatedPrompt.contains("4.3 km away"));
    }

    @Test
    void testFoodNearMe_CategoryDetectedProperly() {
        doReturn(new PlaceService.Coordinates(28.6923809, 77.0916899, "Mangolpuri, Delhi, India"))
                .when(placeService).geocodeLocation(anyString());

        NearbyPlaceForAi restaurant = new NearbyPlaceForAi(
                "place_food_1",
                "Famous King Momos",
                "DDA Market, Block C, Mangolpuri, Delhi",
                28.6925,
                77.0918,
                0.2,
                4.2,
                "restaurant",
                "https://maps.google.com/?cid=123"
        );

        doReturn(List.of(restaurant))
                .when(placeService).searchNearbyForAi(anyDouble(), anyDouble(), eq("food"), anyDouble());

        when(aiProvider.generateResponse(anyString())).thenReturn("Here are great food spots near you.");

        SendMessageRequest request = new SendMessageRequest();
        request.setContent("food near me");
        SendMessageRequest.ClientContext context = new SendMessageRequest.ClientContext();
        context.setCurrentLocation("Mangolpuri, Delhi");
        request.setClientContext(context);

        aiService.handleGuestChat(request, null);

        verify(placeService).searchNearbyForAi(anyDouble(), anyDouble(), eq("food"), anyDouble());
    }

    @Test
    void testShoppingNearMe_CategoryDetectedProperly() {
        doReturn(new PlaceService.Coordinates(28.6923809, 77.0916899, "Mangolpuri, Delhi, India"))
                .when(placeService).geocodeLocation(anyString());

        NearbyPlaceForAi mall = new NearbyPlaceForAi(
                "place_shop_1",
                "City Centre Mall",
                "Swarn Jayanti Park, Sector 10, Rohini, Delhi",
                28.7170,
                77.1144,
                3.5,
                4.3,
                "shopping_mall",
                "https://maps.google.com/?cid=456"
        );

        doReturn(List.of(mall))
                .when(placeService).searchNearbyForAi(anyDouble(), anyDouble(), eq("shopping"), anyDouble());

        when(aiProvider.generateResponse(anyString())).thenReturn("Here are top shopping places near you.");

        SendMessageRequest request = new SendMessageRequest();
        request.setContent("shopping near me");
        SendMessageRequest.ClientContext context = new SendMessageRequest.ClientContext();
        context.setCurrentLocation("Mangolpuri, Delhi");
        request.setClientContext(context);

        aiService.handleGuestChat(request, null);

        verify(placeService).searchNearbyForAi(anyDouble(), anyDouble(), eq("shopping"), anyDouble());
    }

    @Test
    void testHotelsNearMe_CategoryDetectedProperly() {
        doReturn(new PlaceService.Coordinates(28.6923809, 77.0916899, "Mangolpuri, Delhi, India"))
                .when(placeService).geocodeLocation(anyString());

        NearbyPlaceForAi hotel = new NearbyPlaceForAi(
                "place_hotel_1",
                "Hotel Royal",
                "Block O, Mangolpuri, Delhi",
                28.6920,
                77.0915,
                0.3,
                4.0,
                "hotel",
                "https://maps.google.com/?cid=789"
        );

        doReturn(List.of(hotel))
                .when(placeService).searchNearbyForAi(anyDouble(), anyDouble(), eq("hotels"), anyDouble());

        when(aiProvider.generateResponse(anyString())).thenReturn("Here are nearby hotels.");

        SendMessageRequest request = new SendMessageRequest();
        request.setContent("hotels near me");
        SendMessageRequest.ClientContext context = new SendMessageRequest.ClientContext();
        context.setCurrentLocation("Mangolpuri, Delhi");
        request.setClientContext(context);

        aiService.handleGuestChat(request, null);

        verify(placeService).searchNearbyForAi(anyDouble(), anyDouble(), eq("hotels"), anyDouble());
    }

    @Test
    void testShopsNeaarMeForToys_MatchesAndDiscoversToyStores() {
        doReturn(new PlaceService.Coordinates(28.7237, 77.1124, "Rohini, Delhi, India"))
                .when(placeService).geocodeLocation(anyString());

        NearbyPlaceForAi toyStore = new NearbyPlaceForAi(
                "place_toy_1",
                "Toys 'N' Toys",
                "Shop No 3, Csc-5, Sector 8B, Sector 8, Rohini, Delhi",
                28.7180,
                77.1150,
                0.8,
                4.2,
                "toy_store",
                "https://maps.google.com/?cid=toy1"
        );

        doReturn(List.of(toyStore))
                .when(placeService).searchNearbyWithText(eq("toy shop"), anyDouble(), anyDouble(), anyDouble());

        when(aiProvider.generateResponse(anyString())).thenReturn("Here are real toy shops near you.");

        SendMessageRequest request = new SendMessageRequest();
        request.setContent("SHOPS NEAAR ME FOR TOYS"); // Typo 'neaar'
        SendMessageRequest.ClientContext context = new SendMessageRequest.ClientContext();
        context.setCurrentLocation("Rohini, Delhi");
        request.setClientContext(context);

        aiService.handleGuestChat(request, null);

        verify(placeService).searchNearbyWithText(eq("toy shop"), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void testNearMeToilet_DiscoversPublicToilets() {
        doReturn(new PlaceService.Coordinates(28.7237, 77.1124, "Rohini, Delhi, India"))
                .when(placeService).geocodeLocation(anyString());

        NearbyPlaceForAi toilet = new NearbyPlaceForAi(
                "place_toilet_1",
                "Public Toilet",
                "Swarn Jayanti Park, Sector 10, Rohini, Delhi",
                28.7230,
                77.1120,
                0.2,
                4.4,
                "public_bathroom",
                "https://maps.google.com/?cid=toilet1"
        );

        doReturn(List.of(toilet))
                .when(placeService).searchNearbyWithText(eq("public toilet"), anyDouble(), anyDouble(), anyDouble());

        when(aiProvider.generateResponse(anyString())).thenReturn("Here are public toilets near you.");

        SendMessageRequest request = new SendMessageRequest();
        request.setContent("NEAR ME TOILET");
        SendMessageRequest.ClientContext context = new SendMessageRequest.ClientContext();
        context.setCurrentLocation("Rohini, Delhi");
        request.setClientContext(context);

        aiService.handleGuestChat(request, null);

        verify(placeService).searchNearbyWithText(eq("public toilet"), anyDouble(), anyDouble(), anyDouble());
    }
}
