import { useCallback, useState } from "react";
import { tripService } from "@/services/tripService";
import { useAsyncData } from "./useAsyncData";
import { toFriendlyMessage } from "@/services/api";
import type { Trip, TripPayload } from "@/types/trip";

export function useTrips() {
  const loader = useCallback(() => tripService.getAll(), []);
  const state = useAsyncData<Trip[]>(loader, [], { errorMessage: "Unable to load your trips." });
  return {
    trips: state.data ?? [],
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
    isPendingIntegration: false,
  };
}

export function useTrip(id: string) {
  const loader = useCallback(() => tripService.getById(id), [id]);
  const state = useAsyncData<Trip>(loader, [id], { errorMessage: "Unable to load this trip." });
  return {
    trip: state.data,
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
    isPendingIntegration: false,
  };
}

export function useCreateTrip() {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createTrip = useCallback(async (payload: TripPayload): Promise<Trip | null> => {
    setIsSubmitting(true);
    setError(null);
    try {
      return await tripService.create(payload);
    } catch (err: unknown) {
      setError(toFriendlyMessage(err, "Unable to save this trip. Please try again."));
      return null;
    } finally {
      setIsSubmitting(false);
    }
  }, []);

  return { createTrip, isSubmitting, error, isPendingIntegration: false };
}
