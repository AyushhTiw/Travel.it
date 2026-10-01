package com.travelit.place.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelit.place.dto.CreatePlaceRequest;
import com.travelit.place.dto.GooglePlaceDetailsResponse;
import com.travelit.place.dto.GooglePlaceResponse;
import com.travelit.place.dto.NearbyPlaceForAi;
import com.travelit.place.dto.PlaceResponse;
import com.travelit.place.dto.UpdatePlaceRequest;
import com.travelit.place.entity.Place;
import com.travelit.place.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final RestClient googlePlacesClient;
    private final ObjectMapper objectMapper;

    @Value("${google.maps.api-key}")
    private String googleMapsApiKey;

    private static final Set<String> EXCLUDED_TYPES = Set.of(
        // Essential services are NOT excluded (toilets, hospitals, pharmacies)
        // Only exclude business/repair services that travelers don't typically need
        "school","primary_school","secondary_school","university",
        "lawyer","accounting","insurance_agency","real_estate_agency",
        "storage","laundry","gas_station","car_repair","car_wash",
        "electrician","plumber","roofing_contractor","painter","locksmith",
        "moving_company","auto_parts_store","tire_shop","car_dealer",
        "beauty_salon","hair_salon","hair_care","nail_salon","barber_shop",
        "manufacturer","wholesaler","preschool","tour_agency",
        "government_office","post_office","courthouse","embassy","local_government_office",
        "funeral_home","veterinary_care"
    );

    // Category -> included types for nearby search
    private static final String TYPES_ALL =
        "\"tourist_attraction\",\"museum\",\"park\",\"restaurant\"," +
        "\"cafe\",\"shopping_mall\",\"hotel\",\"amusement_park\"," +
        "\"movie_theater\",\"night_club\",\"bar\",\"art_gallery\"," +
        "\"zoo\",\"aquarium\",\"stadium\",\"market\"";

    private static final String TYPES_TOURIST =
        "\"tourist_attraction\",\"museum\",\"art_gallery\"," +
        "\"zoo\",\"aquarium\",\"church\",\"mosque\",\"hindu_temple\"";

    private static final String TYPES_BEST =
        "\"tourist_attraction\",\"museum\",\"art_gallery\"," +
        "\"restaurant\",\"cafe\",\"hotel\",\"amusement_park\"," +
        "\"shopping_mall\",\"park\",\"zoo\"";

    private static final String TYPES_ADVENTURE =
        "\"amusement_park\",\"amusement_center\",\"water_park\",\"bowling_alley\"";

    private static final String TYPES_SPORTS =
        "\"stadium\",\"golf_course\",\"bowling_alley\"";

    private static final String TYPES_FOOD =
        "\"restaurant\",\"cafe\",\"bar\",\"bakery\"," +
        "\"meal_takeaway\",\"meal_delivery\"";

    private static final String TYPES_CAFES =
        "\"cafe\",\"bakery\",\"bar\"";

    private static final String TYPES_HOTELS =
        "\"hotel\",\"lodging\",\"motel\"";

    private static final String TYPES_SHOPPING =
        "\"shopping_mall\",\"market\",\"department_store\"," +
        "\"clothing_store\",\"jewelry_store\",\"book_store\"," +
        "\"electronics_store\",\"shoe_store\",\"home_goods_store\"";

    private static final String TYPES_LUXURY =
        "\"hotel\",\"spa\",\"restaurant\",\"night_club\",\"jewelry_store\"";

    private static final String TYPES_NATURE =
        "\"park\",\"campground\",\"zoo\",\"aquarium\"";

    private static final String TYPES_ENTERTAINMENT =
        "\"amusement_park\",\"movie_theater\",\"night_club\",\"bowling_alley\"";

    private static final String TYPES_FAMILY =
        "\"amusement_park\",\"zoo\",\"aquarium\",\"bowling_alley\"," +
        "\"movie_theater\",\"park\",\"museum\"";

    public PlaceService(PlaceRepository placeRepository, ObjectMapper objectMapper) {
        this.placeRepository = placeRepository;
        this.objectMapper = objectMapper;
        this.googlePlacesClient = RestClient.builder()
            .baseUrl("https://places.googleapis.com/v1").build();
    }

    // =========================================================
    // EXISTING PLACE CRUD
    // =========================================================

    public PlaceResponse createPlace(CreatePlaceRequest request) {
        Place place = new Place(
            request.getName().trim(), clean(request.getDescription()),
            request.getCity().trim(), clean(request.getState()),
            request.getCountry().trim(), request.getLatitude(),
            request.getLongitude(), clean(request.getAddress()));
        return toResponse(placeRepository.save(place));
    }

    public PlaceResponse getPlace(Long id) { return toResponse(findPlace(id)); }

    public List<PlaceResponse> getAllPlaces() {
        return placeRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<PlaceResponse> getPlacesByCity(String city) {
        return placeRepository.findByCityIgnoreCase(city.trim()).stream().map(this::toResponse).toList();
    }

    public List<PlaceResponse> getPlacesByCountry(String country) {
        return placeRepository.findByCountryIgnoreCase(country.trim()).stream().map(this::toResponse).toList();
    }

    public List<PlaceResponse> getActivePlaces() {
        return placeRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public PlaceResponse updatePlace(Long id, UpdatePlaceRequest request) {
        Place place = findPlace(id);
        place.setName(request.getName().trim());
        place.setDescription(clean(request.getDescription()));
        place.setCity(request.getCity().trim());
        place.setState(clean(request.getState()));
        place.setCountry(request.getCountry().trim());
        place.setLatitude(request.getLatitude());
        place.setLongitude(request.getLongitude());
        place.setAddress(clean(request.getAddress()));
        return toResponse(placeRepository.save(place));
    }

    public void deletePlace(Long id) { placeRepository.delete(findPlace(id)); }

    // =========================================================
    // GOOGLE PLACES - TEXT SEARCH (basic)
    // =========================================================

    public List<GooglePlaceResponse> searchGooglePlaces(String query) {
        if (query == null || query.isBlank()) throw new IllegalArgumentException("Search query is required");
        String body = "{\"textQuery\": \"" + query.trim().replace("\\","\\\\").replace("\"","\\\"") + "\", \"pageSize\": 10}";
        String response = googlePlacesClient.post()
            .uri("/places:searchText")
            .header("X-Goog-Api-Key", googleMapsApiKey)
            .header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.primaryType,places.googleMapsUri")
            .header("Content-Type", "application/json")
            .body(body).retrieve().body(String.class);
        try {
            JsonNode root = objectMapper.readTree(response);
            List<GooglePlaceResponse> result = new ArrayList<>();
            JsonNode places = root.path("places");
            if (places.isArray()) for (JsonNode p : places) result.add(mapGooglePlace(p));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Google Places response", e);
        }
    }

    // =========================================================
    // PLACES PAGE SEARCH â€” text query with category filter
    // Used by GET /api/v1/places/search?q=...&category=...
    // When query is blank, falls back to popular tourist attractions
    // =========================================================

    public List<GooglePlaceResponse> searchPlacesPage(String query, String category) {
        String effectiveQuery = buildPlacesPageQuery(query, category);
        System.out.println("[PlacesSearch] query='" + query + "' category='" + category + "' effective='" + effectiveQuery + "'");

        String includedTypesFilter = buildIncludedTypesFilter(category);
        String body;

        // Add location bias for India (Delhi: 28.6139°N, 77.2090°E, 500km radius)
        // Prioritizes results from India while allowing the "India" keyword to work naturally
        if (includedTypesFilter != null && !includedTypesFilter.isBlank()) {
            body = "{\"textQuery\": \"" + escapeJson(effectiveQuery) + "\", " +
                   "\"includedType\": \"" + includedTypesFilter + "\", " +
                   "\"locationBias\": {\"circle\": {\"center\": {\"latitude\": 28.6139, \"longitude\": 77.2090}, \"radius\": 500000.0}}, " +
                   "\"pageSize\": 20}";
        } else {
            body = "{\"textQuery\": \"" + escapeJson(effectiveQuery) + "\", " +
                   "\"locationBias\": {\"circle\": {\"center\": {\"latitude\": 28.6139, \"longitude\": 77.2090}, \"radius\": 500000.0}}, " +
                   "\"pageSize\": 20}";
        }

        try {
            String response = googlePlacesClient.post()
                .uri("/places:searchText")
                .header("X-Goog-Api-Key", googleMapsApiKey)
                .header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.primaryType,places.googleMapsUri")
                .header("Content-Type", "application/json")
                .body(body).retrieve().body(String.class);

            JsonNode root = objectMapper.readTree(response);
            List<GooglePlaceResponse> result = new ArrayList<>();
            JsonNode places = root.path("places");
            if (places.isArray()) {
                for (JsonNode p : places) {
                    GooglePlaceResponse mapped = mapGooglePlace(p);
                    if (!isExcludedType(mapped.getPrimaryType())) result.add(mapped);
                }
            }
            System.out.println("[PlacesSearch] returning " + result.size() + " places");
            return result;
        } catch (Exception e) {
            System.err.println("[PlacesSearch] ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to search places: " + e.getMessage(), e);
        }
    }

    private String buildPlacesPageQuery(String query, String category) {
        boolean hasQuery = query != null && !query.isBlank();
        boolean hasCategory = category != null && !category.isBlank() && !"all".equalsIgnoreCase(category);

        if (hasQuery && hasCategory) {
            return query.trim() + " " + categoryToSearchTerm(category) + " India";
        }
        if (hasQuery) {
            return query.trim() + " India tourist place";
        }
        if (hasCategory) {
            return "best " + categoryToSearchTerm(category) + " places India";
        }
        return "famous tourist attractions monuments India";
    }

    private String categoryToSearchTerm(String category) {
        if (category == null) return "tourist";
        return switch (category.toLowerCase().trim()) {
            case "tourist_spots" -> "tourist attraction monument landmark";
            case "best_spots"    -> "popular famous tourist spot";
            case "adventure"     -> "adventure park outdoor activity";
            case "sports"        -> "stadium sports complex";
            case "food"          -> "restaurant food";
            case "cafes"         -> "cafe coffee";
            case "hotels"        -> "hotel resort";
            case "shopping"      -> "shopping mall market";
            case "luxury"        -> "luxury hotel resort";
            case "nature"        -> "national park nature reserve";
            case "entertainment" -> "amusement park entertainment";
            case "family"        -> "family park zoo";
            default              -> "tourist";
        };
    }

    private String buildIncludedTypesFilter(String category) {
        if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) return null;
        return switch (category.toLowerCase().trim()) {
            case "tourist_spots" -> "tourist_attraction";
            case "best_spots"    -> "tourist_attraction";
            case "adventure"     -> "amusement_park";
            case "sports"        -> "stadium";
            case "food"          -> "restaurant";
            case "cafes"         -> "cafe";
            case "hotels"        -> "hotel";
            case "shopping"      -> "shopping_mall";
            case "luxury"        -> "hotel";
            case "nature"        -> "park";
            case "entertainment" -> "amusement_park";
            case "family"        -> "zoo";
            default              -> null;
        };
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // =========================================================
    // GOOGLE PLACES - NEARBY SEARCH (category-based, for Explore)
    // =========================================================

    public List<GooglePlaceResponse> searchNearbyPlaces(
            double latitude, double longitude, double radius, String category) {

        if (latitude < -90 || latitude > 90) throw new IllegalArgumentException("Invalid latitude");
        if (longitude < -180 || longitude > 180) throw new IllegalArgumentException("Invalid longitude");
        if (radius <= 0 || radius > 50000) throw new IllegalArgumentException("Radius must be between 1 and 50000 meters");

        String typesJson = resolveTypes(category);
        boolean isBestSpots = "best_spots".equalsIgnoreCase(category);
        String rankPref = isBestSpots ? "POPULARITY" : "DISTANCE";

        String body = String.format(
            "{\"includedTypes\":[%s],\"maxResultCount\":20,\"rankPreference\":\"%s\"," +
            "\"locationRestriction\":{\"circle\":{\"center\":{\"latitude\":%f,\"longitude\":%f},\"radius\":%f}}}",
            typesJson, rankPref, latitude, longitude, radius);

        System.out.println("[NearbySearch] cat=" + category + " lat=" + latitude + " lng=" + longitude + " radius=" + radius + "m");

        try {
            String response = googlePlacesClient.post()
                .uri("/places:searchNearby")
                .header("X-Goog-Api-Key", googleMapsApiKey)
                .header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.primaryType,places.googleMapsUri")
                .header("Content-Type", "application/json")
                .body(body).retrieve().body(String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode places = root.path("places");
            List<GooglePlaceResponse> result = new ArrayList<>();
            if (places.isArray()) {
                for (JsonNode p : places) {
                    GooglePlaceResponse mapped = mapGooglePlace(p);
                    if (!isExcludedType(mapped.getPrimaryType())) result.add(mapped);
                }
            }
            System.out.println("[NearbySearch] returning " + result.size() + " places");
            return result;
        } catch (Exception e) {
            System.err.println("[NearbySearch] ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to search nearby places: " + e.getMessage(), e);
        }
    }

    private String resolveTypes(String category) {
        if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) return TYPES_ALL;
        return switch (category.toLowerCase().trim()) {
            case "tourist_spots" -> TYPES_TOURIST;
            case "best_spots"    -> TYPES_BEST;
            case "adventure"     -> TYPES_ADVENTURE;
            case "sports"        -> TYPES_SPORTS;
            case "food"          -> TYPES_FOOD;
            case "cafes"         -> TYPES_CAFES;
            case "hotels"        -> TYPES_HOTELS;
            case "shopping"      -> TYPES_SHOPPING;
            case "luxury"        -> TYPES_LUXURY;
            case "nature"        -> TYPES_NATURE;
            case "entertainment" -> TYPES_ENTERTAINMENT;
            case "family"        -> TYPES_FAMILY;
            default              -> TYPES_ALL;
        };
    }

    private boolean isExcludedType(String primaryType) {
        if (primaryType == null) return false;
        return EXCLUDED_TYPES.stream().anyMatch(primaryType.toLowerCase()::contains);
    }

    // =========================================================
    // GOOGLE PLACES - PLACE DETAILS
    // =========================================================

    public GooglePlaceDetailsResponse getGooglePlaceDetails(String placeId) {
        if (placeId == null || placeId.isBlank()) throw new IllegalArgumentException("Place ID is required");
        String response = googlePlacesClient.get()
            .uri("/places/{placeId}", placeId.trim())
            .header("X-Goog-Api-Key", googleMapsApiKey)
            .header("X-Goog-FieldMask", "id,displayName,formattedAddress,location,rating,primaryType,nationalPhoneNumber,internationalPhoneNumber,websiteUri,googleMapsUri,businessStatus,regularOpeningHours.openNow,regularOpeningHours.weekdayDescriptions")
            .header("Content-Type", "application/json")
            .retrieve().body(String.class);
        try {
            JsonNode place = objectMapper.readTree(response);
            JsonNode dn = place.path("displayName");
            JsonNode loc = place.path("location");
            JsonNode oh = place.path("regularOpeningHours");
            List<String> weekdays = new ArrayList<>();
            JsonNode wn = oh.path("weekdayDescriptions");
            if (wn.isArray()) for (JsonNode d : wn) weekdays.add(d.asText());
            Boolean openNow = oh.has("openNow") ? oh.path("openNow").asBoolean() : null;
            return new GooglePlaceDetailsResponse(
                place.path("id").asText(null), dn.path("text").asText(null),
                place.path("formattedAddress").asText(null),
                loc.path("latitude").isNumber() ? loc.path("latitude").asDouble() : null,
                loc.path("longitude").isNumber() ? loc.path("longitude").asDouble() : null,
                place.path("rating").isNumber() ? place.path("rating").asDouble() : null,
                place.path("primaryType").asText(null),
                place.path("nationalPhoneNumber").asText(null),
                place.path("internationalPhoneNumber").asText(null),
                place.path("websiteUri").asText(null),
                place.path("googleMapsUri").asText(null),
                place.path("businessStatus").asText(null),
                openNow, weekdays);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Google Place Details response", e);
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private GooglePlaceResponse mapGooglePlace(JsonNode place) {
        JsonNode dn = place.path("displayName");
        JsonNode loc = place.path("location");
        String googleMapsUri = place.has("googleMapsUri") ? place.path("googleMapsUri").asText(null) : null;

        // Build maps URL: prefer googleMapsUri, else build from coords
        String mapsUrl = googleMapsUri;
        Double lat = loc.path("latitude").isNumber() ? loc.path("latitude").asDouble() : null;
        Double lng = loc.path("longitude").isNumber() ? loc.path("longitude").asDouble() : null;
        if (mapsUrl == null && lat != null && lng != null) {
            mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng;
        }

        GooglePlaceResponse r = new GooglePlaceResponse(
            place.path("id").asText(null),
            dn.path("text").asText(null),
            place.path("formattedAddress").asText(null),
            lat, lng,
            place.path("rating").isNumber() ? place.path("rating").asDouble() : null,
            place.path("primaryType").asText(null));
        r.setGoogleMapsUrl(mapsUrl);
        return r;
    }

    private Place findPlace(Long id) {
        return placeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Place not found"));
    }

    private String clean(String value) {
        if (value == null) return null;
        String c = value.trim();
        return c.isEmpty() ? null : c;
    }

    private PlaceResponse toResponse(Place place) {
        return new PlaceResponse(place.getId(), place.getName(), place.getDescription(),
            place.getCity(), place.getState(), place.getCountry(),
            place.getLatitude(), place.getLongitude(), place.getAddress(), place.isActive());
    }

    // =========================================================
    // AI INTEGRATION â€” GEOCODING & HIGH-PRECISION NEARBY SEARCH
    // =========================================================

    public record Coordinates(double latitude, double longitude, String formattedAddress) {}

    public Coordinates geocodeLocation(String locationName) {
        if (locationName == null || locationName.isBlank()) return null;
        try {
            List<GooglePlaceResponse> results = searchGooglePlaces(locationName.trim());
            if (results != null && !results.isEmpty()) {
                GooglePlaceResponse first = results.get(0);
                if (first.getLatitude() != null && first.getLongitude() != null) {
                    return new Coordinates(first.getLatitude(), first.getLongitude(), first.getAddress());
                }
            }
        } catch (Exception e) {
            System.err.println("[Geocode] Failed to geocode '" + locationName + "': " + e.getMessage());
        }
        return null;
    }

    public List<NearbyPlaceForAi> searchNearbyForAi(
            double latitude, double longitude, String category, double initialRadiusMeters) {

        double radius = initialRadiusMeters > 0 ? initialRadiusMeters : 5000.0;
        List<GooglePlaceResponse> places = searchNearbyPlaces(latitude, longitude, radius, category);
        List<NearbyPlaceForAi> filtered = filterAndMapForAi(places, latitude, longitude);

        // If results are sparse (< 3) for adventure, expand radius to 10,000m
        if (filtered.size() < 3 && radius < 10000.0) {
            System.out.println("[NearbyForAi] Only " + filtered.size() + " places found at " + radius + "m, expanding to 10000m...");
            List<GooglePlaceResponse> expanded = searchNearbyPlaces(latitude, longitude, 10000.0, category);
            List<NearbyPlaceForAi> expandedFiltered = filterAndMapForAi(expanded, latitude, longitude);
            if (expandedFiltered.size() > filtered.size()) {
                filtered = expandedFiltered;
            }
        }

        // Sort primarily by distance ascending, secondarily by rating descending
        filtered.sort((a, b) -> {
            int distCompare = Double.compare(
                a.getDistanceKm() != null ? a.getDistanceKm() : 999.0,
                b.getDistanceKm() != null ? b.getDistanceKm() : 999.0
            );
            if (distCompare != 0) return distCompare;
            return Double.compare(
                b.getRating() != null ? b.getRating() : 0.0,
                a.getRating() != null ? a.getRating() : 0.0
            );
        });

        return filtered.stream().limit(8).toList();
    }

    public List<NearbyPlaceForAi> searchNearbyWithText(
            String textQuery, double latitude, double longitude, double radiusMeters) {
        if (textQuery == null || textQuery.isBlank()) {
            return searchNearbyForAi(latitude, longitude, "all", radiusMeters);
        }

        double radius = radiusMeters > 0 ? radiusMeters : 5000.0;
        String body = String.format(
            "{\"textQuery\":\"%s\",\"maxResultCount\":10,\"locationBias\":{\"circle\":{\"center\":{\"latitude\":%f,\"longitude\":%f},\"radius\":%f}}}",
            escapeJson(textQuery), latitude, longitude, radius);

        try {
            String response = googlePlacesClient.post()
                .uri("/places:searchText")
                .header("X-Goog-Api-Key", googleMapsApiKey)
                .header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.primaryType,places.googleMapsUri")
                .header("Content-Type", "application/json")
                .body(body).retrieve().body(String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode places = root.path("places");
            List<GooglePlaceResponse> list = new ArrayList<>();
            boolean isToiletSearch = textQuery.toLowerCase().contains("toilet")
                    || textQuery.toLowerCase().contains("restroom")
                    || textQuery.toLowerCase().contains("washroom");

            if (places.isArray()) {
                for (JsonNode p : places) {
                    GooglePlaceResponse mapped = mapGooglePlace(p);
                    if (isToiletSearch || !isExcludedType(mapped.getPrimaryType())) {
                        list.add(mapped);
                    }
                }
            }

            List<NearbyPlaceForAi> filtered = filterAndMapForAi(list, latitude, longitude, isToiletSearch);
            filtered.sort((a, b) -> Double.compare(
                a.getDistanceKm() != null ? a.getDistanceKm() : 999.0,
                b.getDistanceKm() != null ? b.getDistanceKm() : 999.0
            ));
            return filtered.stream().limit(8).toList();
        } catch (Exception e) {
            System.err.println("[SearchNearbyWithText] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<NearbyPlaceForAi> filterAndMapForAi(
            List<GooglePlaceResponse> places, double userLat, double userLng) {
        return filterAndMapForAi(places, userLat, userLng, false);
    }

    private List<NearbyPlaceForAi> filterAndMapForAi(
            List<GooglePlaceResponse> places, double userLat, double userLng, boolean allowToilets) {
        if (places == null) return new ArrayList<>();

        List<NearbyPlaceForAi> result = new ArrayList<>();
        Set<String> seenNames = new java.util.HashSet<>();

        for (GooglePlaceResponse p : places) {
            if (p.getName() == null || p.getName().isBlank()) continue;
            String lowerName = p.getName().toLowerCase();

            // Exclude noise/unwanted keywords unless searching specifically for it
            if (lowerName.contains("manufacturer") || lowerName.contains("supplier")
                    || lowerName.contains("wholesale") || lowerName.contains("play school")
                    || lowerName.contains("preschool") || lowerName.contains("inflatable")
                    || lowerName.contains("tour agency") || lowerName.contains("holidays")) {
                continue;
            }

            // Exclude unwanted business types
            if (!allowToilets && isExcludedType(p.getPrimaryType())) {
                continue;
            }

            // Deduplicate similar names
            String normalizedKey = lowerName.replaceAll("[^a-z0-9]", "");
            if (seenNames.contains(normalizedKey)) continue;
            seenNames.add(normalizedKey);

            double dist = 0.0;
            if (p.getLatitude() != null && p.getLongitude() != null) {
                dist = calculateHaversineKm(userLat, userLng, p.getLatitude(), p.getLongitude());
            }

            result.add(new NearbyPlaceForAi(
                p.getPlaceId(),
                p.getName(),
                p.getAddress(),
                p.getLatitude(),
                p.getLongitude(),
                dist,
                p.getRating(),
                p.getPrimaryType(),
                p.getGoogleMapsUrl()
            ));
        }
        return result;
    }

    private double calculateHaversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(6371.0 * c * 10.0) / 10.0;
    }
}