import { useCallback, useEffect, useRef, useState } from "react";

import { AppLayout } from "@/components/layout/AppLayout";
import { DestinationGrid } from "@/components/destination/DestinationGrid";
import { ExploreSearch } from "@/components/explore/ExploreSearch";
import { destinationService } from "@/services/destinationService";
import { toFriendlyMessage } from "@/services/api";
import type { Destination } from "@/types/destination";

const DEBOUNCE_MS = 300;

export function DestinationsPage() {
  const [destinations, setDestinations] = useState<Destination[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [query, setQuery] = useState("");
  
  const requestIdRef = useRef(0);
  const debounceTimerRef = useRef<NodeJS.Timeout | null>(null);

  const fetchDestinations = useCallback(async (searchTerm: string) => {
    const id = ++requestIdRef.current;
    const trimmed = searchTerm.trim();
    
    console.log(`[Destinations] Fetching - search="${trimmed}"`);
    setIsLoading(true);
    setError(null);

    try {
      const results = trimmed
        ? await destinationService.search(trimmed)
        : await destinationService.getAll();

      if (id !== requestIdRef.current) {
        console.log(`[Destinations] #${id} stale - ignoring`);
        return;
      }

      console.log(`[Destinations] #${id} got ${results.length} destinations`);
      setDestinations(results);
    } catch (err) {
      if (id !== requestIdRef.current) return;
      console.error("[Destinations] Error:", err);
      setError(toFriendlyMessage(err, "Unable to load destinations."));
      setDestinations([]);
    } finally {
      if (id === requestIdRef.current) {
        setIsLoading(false);
      }
    }
  }, []);

  // Debounced search effect
  useEffect(() => {
    if (debounceTimerRef.current) {
      clearTimeout(debounceTimerRef.current);
    }

    debounceTimerRef.current = setTimeout(() => {
      void fetchDestinations(query);
    }, DEBOUNCE_MS);

    return () => {
      if (debounceTimerRef.current) {
        clearTimeout(debounceTimerRef.current);
      }
    };
  }, [query, fetchDestinations]);

  const handleRetry = () => {
    void fetchDestinations(query);
  };

  return (
    <AppLayout title="Destinations" description="Browse the places Travel.it can plan around.">
      <div className="space-y-8">
        <div className="max-w-md">
          <ExploreSearch
            value={query}
            onChange={setQuery}
            label="Search destinations"
            placeholder="Search by name, country or state"
          />
        </div>
        <DestinationGrid
          destinations={destinations}
          isLoading={isLoading}
          error={error}
          onRetry={handleRetry}
          emptyTitle={query ? "No matches" : "No destinations yet"}
          emptyDescription={
            query ? "Try a different search term." : "Once destinations are added they''ll show up here."
          }
        />
      </div>
    </AppLayout>
  );
}
