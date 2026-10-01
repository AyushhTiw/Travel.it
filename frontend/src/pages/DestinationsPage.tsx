import { useCallback, useEffect, useRef, useState } from "react";
import { ArrowUpRight, Map, MapPin, Star } from "lucide-react";

import { AppLayout } from "@/components/layout/AppLayout";
import { CardSkeletonGrid } from "@/components/common/Loader";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { ExploreSearch } from "@/components/explore/ExploreSearch";
import { placesService } from "@/services/placesService";
import { toFriendlyMessage } from "@/services/api";
import type { GooglePlace } from "@/types/places";

const DEBOUNCE_MS = 300;

export function DestinationsPage() {
  const [places, setPlaces] = useState<GooglePlace[]>([]);
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
      const results = await placesService.searchPlaces({
        q: trimmed,
        category: "tourist_spots"
      });

      if (id !== requestIdRef.current) {
        console.log(`[Destinations] #${id} stale - ignoring`);
        return;
      }

      console.log(`[Destinations] #${id} got ${results.length} places`);
      setPlaces(results);
    } catch (err) {
      if (id !== requestIdRef.current) return;
      console.error("[Destinations] Error:", err);
      setError(toFriendlyMessage(err, "Unable to load destinations."));
      setPlaces([]);
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
            placeholder="Search landmarks, cities, or attractions"
          />
        </div>
        
        {isLoading ? (
          <CardSkeletonGrid />
        ) : error ? (
          <ErrorMessage message={error} onRetry={handleRetry} />
        ) : places.length === 0 ? (
          <EmptyState 
            icon={Map} 
            title={query ? "No matches" : "Search for destinations"} 
            description={
              query 
                ? "Try a different search term." 
                : "Search for famous landmarks, tourist attractions, or cities to explore."
            } 
          />
        ) : (
          <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {places.map((place) => (
              <DestinationPlaceCard key={place.placeId} place={place} />
            ))}
          </div>
        )}
      </div>
    </AppLayout>
  );
}

function DestinationPlaceCard({ place }: { place: GooglePlace }) {
  // Extract location from address (last 2 parts after commas)
  const addressParts = place.address.split(',').map(part => part.trim());
  const location = addressParts.length >= 2 
    ? addressParts.slice(-2).join(', ') 
    : place.address;

  return (
    <a
      href={place.googleMapsUrl || `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(place.name)}&query_place_id=${place.placeId}`}
      target="_blank"
      rel="noopener noreferrer"
      className="group flex flex-col rounded-3xl border border-border bg-card p-6 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg"
    >
      <div className="grid grid-cols-[minmax(0,1fr)_auto] items-start gap-3">
        <div className="min-w-0">
          <h3 className="truncate text-lg font-semibold text-foreground">{place.name}</h3>
          <p className="mt-1 flex min-w-0 items-center gap-1.5 text-sm text-muted-foreground">
            <MapPin className="size-3.5 shrink-0 text-primary" aria-hidden />
            <span className="truncate">{location}</span>
          </p>
          {place.rating && (
            <p className="mt-1 flex items-center gap-1 text-sm text-amber-600">
              <Star className="size-3.5 fill-current" aria-hidden />
              <span className="font-medium">{place.rating.toFixed(1)}</span>
            </p>
          )}
        </div>
        <span className="grid size-9 shrink-0 place-items-center rounded-full bg-secondary text-primary transition-colors group-hover:bg-primary group-hover:text-primary-foreground">
          <ArrowUpRight className="size-4" aria-hidden />
        </span>
      </div>
      {place.primaryType && (
        <p className="mt-4 text-xs uppercase tracking-wide text-muted-foreground">
          {place.primaryType.replace(/_/g, ' ')}
        </p>
      )}
    </a>
  );
}