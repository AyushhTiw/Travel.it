import { useCallback } from "react";

import { placeService } from "@/services/placeService";
import { useAsyncData } from "./useAsyncData";
import type { Place } from "@/types/place";

export function usePlaces(options: { activeOnly?: boolean; city?: string; country?: string } = {}) {
  const { activeOnly = true, city, country } = options;

  const loader = useCallback(() => {
    if (city) return placeService.getByCity(city);
    if (country) return placeService.getByCountry(country);
    return activeOnly ? placeService.getActive() : placeService.getAll();
  }, [activeOnly, city, country]);

  const state = useAsyncData<Place[]>(loader, [activeOnly, city, country], {
    errorMessage: "Unable to load places.",
  });

  return {
    places: state.data ?? [],
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
  };
}

export function usePlace(id: string) {
  const loader = useCallback(() => placeService.getById(id), [id]);
  const state = useAsyncData<Place>(loader, [id], {
    errorMessage: "Unable to load this place.",
  });

  return {
    place: state.data,
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
  };
}
