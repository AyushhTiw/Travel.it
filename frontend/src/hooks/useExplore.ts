import { useCallback, useState } from "react";

import { exploreService } from "@/services/exploreService";
import { toFriendlyMessage } from "@/services/api";
import type { Coordinates, NearbyPlace } from "@/types/explore";

export interface UseExploreResult {
  places: NearbyPlace[] | null;
  isLoading: boolean;
  error: string | null;
  coordinates: Coordinates | null;
  isLocating: boolean;
  locationError: string | null;
  search: (coordinates: Coordinates, radiusKm: number) => Promise<void>;
  requestLocation: () => Promise<Coordinates | null>;
  reset: () => void;
}

/**
 * Nearby discovery. Geolocation is only requested when requestLocation() is
 * called from a user action — never on mount.
 */
export function useExplore(): UseExploreResult {
  const [places, setPlaces] = useState<NearbyPlace[] | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [coordinates, setCoordinates] = useState<Coordinates | null>(null);
  const [isLocating, setIsLocating] = useState(false);
  const [locationError, setLocationError] = useState<string | null>(null);

  const search = useCallback(async (coords: Coordinates, radiusKm: number) => {
    setIsLoading(true);
    setError(null);
    setCoordinates(coords);
    try {
      const result = await exploreService.getNearby({ ...coords, radiusKm });
      setPlaces(result);
    } catch (err: unknown) {
      setError(toFriendlyMessage(err, "Unable to load nearby places."));
      setPlaces(null);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const requestLocation = useCallback((): Promise<Coordinates | null> => {
    setLocationError(null);
    if (typeof navigator === "undefined" || !navigator.geolocation) {
      setLocationError("Location isn't available in this browser.");
      return Promise.resolve(null);
    }

    setIsLocating(true);
    return new Promise<Coordinates | null>((resolve) => {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const coords: Coordinates = {
            latitude: Number(position.coords.latitude.toFixed(6)),
            longitude: Number(position.coords.longitude.toFixed(6)),
          };
          try {
            sessionStorage.setItem("travelit_user_coords", JSON.stringify(coords));
          } catch {}
          setCoordinates(coords);
          setIsLocating(false);
          resolve(coords);
        },
        () => {
          setLocationError("We couldn't get your location. Allow access or enter coordinates.");
          setIsLocating(false);
          resolve(null);
        },
        { enableHighAccuracy: true, timeout: 12_000 },
      );
    });
  }, []);

  const reset = useCallback(() => {
    setPlaces(null);
    setError(null);
    setCoordinates(null);
    setLocationError(null);
  }, []);

  return {
    places,
    isLoading,
    error,
    coordinates,
    isLocating,
    locationError,
    search,
    requestLocation,
    reset,
  };
}
