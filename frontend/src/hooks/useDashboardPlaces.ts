import { useCallback, useEffect, useRef, useState } from "react";
import { placesService } from "@/services/placesService";
import { toFriendlyMessage } from "@/services/api";
import type { GooglePlace } from "@/types/places";

const DEFAULT_COORDS = { lat: 28.6139, lng: 77.209 }; // Delhi fallback
const DASHBOARD_RADIUS_M = 2000; // 2 km priority radius
const DASHBOARD_MAX_PLACES = 5;

// Travel-relevant categories for dashboard
const TRAVEL_CATEGORIES = [
  "tourist_spots",
  "best_spots",
  "food",
  "cafes"
];

export interface UseDashboardPlacesResult {
  places: GooglePlace[];
  isLoading: boolean;
  error: string | null;
  retry: () => void;
}

/**
 * Hook for fetching nearby travel-relevant places for Dashboard.
 * Prioritizes places within 2km, shows max 5 curated results.
 */
export function useDashboardPlaces(
  userLat?: number | null,
  userLng?: number | null
): UseDashboardPlacesResult {
  const [places, setPlaces] = useState<GooglePlace[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const requestIdRef = useRef(0);

  const fetchPlaces = useCallback(async () => {
    const lat = userLat ?? DEFAULT_COORDS.lat;
    const lng = userLng ?? DEFAULT_COORDS.lng;
    const id = ++requestIdRef.current;

    console.log(`[Dashboard] Fetching nearby places - lat=${lat.toFixed(4)}, lng=${lng.toFixed(4)}, radius=${DASHBOARD_RADIUS_M}m`);

    setIsLoading(true);
    setError(null);

    try {
      // Fetch tourist_spots as primary category for dashboard
      const results = await placesService.nearbySearch({
        latitude: lat,
        longitude: lng,
        radius: DASHBOARD_RADIUS_M,
        category: "tourist_spots", // Focus on travel-relevant places
      });

      if (id !== requestIdRef.current) {
        console.log(`[Dashboard] #${id} stale - ignoring`);
        return;
      }

      console.log(`[Dashboard] #${id} got ${results.length} places`);

      // Limit to top 5 places
      const limitedPlaces = results.slice(0, DASHBOARD_MAX_PLACES);
      setPlaces(limitedPlaces);
    } catch (err) {
      if (id !== requestIdRef.current) return;
      console.error("[Dashboard] Error fetching places:", err);
      setError(toFriendlyMessage(err, "Unable to load nearby places."));
      setPlaces([]);
    } finally {
      if (id === requestIdRef.current) {
        setIsLoading(false);
      }
    }
  }, [userLat, userLng]);

  // Fetch on mount or when location changes
  useEffect(() => {
    void fetchPlaces();
  }, [fetchPlaces]);

  return {
    places,
    isLoading,
    error,
    retry: fetchPlaces,
  };
}
