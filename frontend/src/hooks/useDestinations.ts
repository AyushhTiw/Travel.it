import { useCallback } from "react";

import { destinationService } from "@/services/destinationService";
import { useAsyncData } from "./useAsyncData";
import type { Destination } from "@/types/destination";

export function useDestinations() {
  const loader = useCallback(() => destinationService.getAll(), []);
  const state = useAsyncData<Destination[]>(loader, [], {
    errorMessage: "Unable to load destinations.",
  });

  return {
    destinations: state.data ?? [],
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
  };
}

export function useDestination(id: string) {
  const loader = useCallback(() => destinationService.getById(id), [id]);
  const state = useAsyncData<Destination>(loader, [id], {
    errorMessage: "Unable to load this destination.",
  });

  return {
    destination: state.data,
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
  };
}
