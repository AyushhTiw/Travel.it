import { useCallback, useEffect, useRef, useState } from "react";
import { MapPin, ExternalLink, Star } from "lucide-react";

import { AppLayout } from "@/components/layout/AppLayout";
import { ExploreSearch } from "@/components/explore/ExploreSearch";
import { TravelCategoryFilter, CATEGORIES } from "@/components/explore/TravelCategoryFilter";
import { CardSkeletonGrid } from "@/components/common/Loader";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { placesService } from "@/services/placesService";
import { toFriendlyMessage } from "@/services/api";
import type { GooglePlace } from "@/types/places";

function formatType(type: string | null): string {
  if (!type) return "Place";
  return type.replace(/_/g, " ").replace(/\b\w/g, (c) => c.toUpperCase());
}

function getMapsUrl(place: GooglePlace): string {
  if (place.googleMapsUrl) return place.googleMapsUrl;
  if (place.latitude && place.longitude)
    return `https://www.google.com/maps/search/?api=1&query=${place.latitude},${place.longitude}`;
  return `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(place.name + " " + (place.address ?? ""))}`;
}

function PlaceCard({ place }: { place: GooglePlace }) {
  return (
    <div className="group flex flex-col rounded-3xl border border-border bg-card p-5 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg hover:border-primary/20">
      <div className="flex items-start justify-between gap-2">
        <h3 className="line-clamp-2 text-base font-semibold text-foreground leading-snug">{place.name}</h3>
      </div>

      {place.primaryType && (
        <span className="mt-2 inline-block self-start rounded-full bg-primary/10 px-2.5 py-0.5 text-[11px] font-medium text-primary">
          {formatType(place.primaryType)}
        </span>
      )}

      {place.address && (
        <p className="mt-2.5 flex min-w-0 items-start gap-1.5 text-xs text-muted-foreground">
          <MapPin className="mt-0.5 size-3 shrink-0 text-primary" aria-hidden />
          <span className="line-clamp-2">{place.address}</span>
        </p>
      )}

      {place.rating != null && (
        <div className="mt-2.5 flex items-center gap-1">
          <Star className="size-3.5 fill-amber-400 text-amber-400" aria-hidden />
          <span className="text-xs font-semibold text-foreground">{place.rating.toFixed(1)}</span>
        </div>
      )}

      <a
        href={getMapsUrl(place)}
        target="_blank"
        rel="noopener noreferrer"
        onClick={(e) => e.stopPropagation()}
        className="mt-4 flex items-center gap-1.5 self-start rounded-full border border-border bg-background px-3 py-1.5 text-xs font-medium text-foreground transition-all hover:border-primary hover:bg-primary/5 hover:text-primary"
      >
        <ExternalLink className="size-3" aria-hidden />
        Open in Maps
      </a>
    </div>
  );
}

export function PlacesPage() {
  const [query, setQuery] = useState("");
  const [category, setCategory] = useState("all");
  const [places, setPlaces] = useState<GooglePlace[] | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Debounce + race-condition prevention
  const debounceRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const requestIdRef = useRef(0);

  const fetchPlaces = useCallback(async (q: string, cat: string) => {
    const id = ++requestIdRef.current;
    setIsLoading(true);
    setError(null);
    try {
      const results = await placesService.searchPlaces({ q, category: cat });
      if (id !== requestIdRef.current) return; // stale response
      setPlaces(results);
    } catch (err) {
      if (id !== requestIdRef.current) return;
      setError(toFriendlyMessage(err, "Unable to load places. Please try again."));
      setPlaces([]);
    } finally {
      if (id === requestIdRef.current) setIsLoading(false);
    }
  }, []);

  // Initial load
  useEffect(() => {
    void fetchPlaces("", "all");
  }, [fetchPlaces]);

  // Debounced search on query/category change
  const handleQueryChange = (val: string) => {
    setQuery(val);
    if (debounceRef.current) clearTimeout(debounceRef.current);
    debounceRef.current = setTimeout(() => void fetchPlaces(val, category), 500);
  };

  const handleCategoryChange = (cat: string) => {
    setCategory(cat);
    if (debounceRef.current) clearTimeout(debounceRef.current);
    void fetchPlaces(query, cat);
  };

  const categoryLabel = CATEGORIES.find((c) => c.id === category)?.label ?? "Places";

  return (
    <AppLayout title="Places" description="Discover travel destinations, landmarks and attractions.">
      <div className="space-y-6">
        {/* Search */}
        <div className="max-w-md">
          <ExploreSearch
            value={query}
            onChange={handleQueryChange}
            label="Search places"
            placeholder="Search Lal Qila, India Gate, Jaipur..."
          />
        </div>

        {/* Category filter */}
        <TravelCategoryFilter selected={category} onChange={handleCategoryChange} />

        {/* Count */}
        {places !== null && !isLoading && (
          <p className="text-sm text-muted-foreground">
            <span className="font-medium text-foreground">{places.length}</span>
            {" "}{categoryLabel} place{places.length !== 1 ? "s" : ""} found
            {query && <span> for &ldquo;<span className="font-medium text-foreground">{query}</span>&rdquo;</span>}
          </p>
        )}

        {/* Results */}
        {isLoading ? (
          <CardSkeletonGrid count={6} />
        ) : error ? (
          <ErrorMessage message={error} onRetry={() => void fetchPlaces(query, category)} />
        ) : places === null ? null : places.length === 0 ? (
          <EmptyState
            icon={MapPin}
            title={query ? `No results for "${query}"` : `No ${categoryLabel} places found`}
            description="Try a different search term or category."
          />
        ) : (
          <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {places.map((place) => (
              <PlaceCard key={place.placeId} place={place} />
            ))}
          </div>
        )}
      </div>
    </AppLayout>
  );
}
