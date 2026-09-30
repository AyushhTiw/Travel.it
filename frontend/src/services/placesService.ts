import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type {
  GooglePlace,
  GooglePlaceDetails,
  GooglePlacesTextSearchQuery,
  GooglePlacesNearbyQuery,
  GooglePlacesSearchQuery,
} from "@/types/places";

export const placesService = {
  // Places page search — real server-side search via Google Places
  async searchPlaces(query: GooglePlacesSearchQuery): Promise<GooglePlace[]> {
    const { data } = await api.get<GooglePlace[]>(ENDPOINTS.PLACES.SEARCH, {
      params: {
        q: query.q ?? "",
        category: query.category ?? "all",
      },
    });
    return data;
  },

  async textSearch(query: GooglePlacesTextSearchQuery): Promise<GooglePlace[]> {
    const { data } = await api.get<GooglePlace[]>(ENDPOINTS.GOOGLE_PLACES.TEXT_SEARCH, {
      params: { query: query.query },
    });
    return data;
  },

  async nearbySearch(query: GooglePlacesNearbyQuery): Promise<GooglePlace[]> {
    const { data } = await api.get<GooglePlace[]>(ENDPOINTS.GOOGLE_PLACES.NEARBY_SEARCH, {
      params: {
        latitude: query.latitude,
        longitude: query.longitude,
        radius: query.radius,
        ...(query.category && query.category !== "all" ? { category: query.category } : {}),
      },
    });
    return data;
  },

  async getPlaceDetails(placeId: string): Promise<GooglePlaceDetails> {
    const { data } = await api.get<GooglePlaceDetails>(ENDPOINTS.GOOGLE_PLACES.DETAILS(placeId));
    return data;
  },
};
