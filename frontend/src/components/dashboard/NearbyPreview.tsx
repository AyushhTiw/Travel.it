import { Link } from "@tanstack/react-router";
import { Compass } from "lucide-react";

import { Button } from "@/components/common/Button";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { CardSkeletonGrid } from "@/components/common/Loader";
import type { GooglePlace } from "@/types/places";

export interface NearbyPreviewProps {
  places: GooglePlace[];
  isLoading: boolean;
  error: string | null;
  onRetry?: () => void;
}

export function NearbyPreview({ places, isLoading, error, onRetry }: NearbyPreviewProps) {
  return (
    <section aria-labelledby="nearby-preview-heading">
      <div className="mb-4 grid grid-cols-[minmax(0,1fr)_auto] items-center gap-3">
        <h2
          id="nearby-preview-heading"
          className="truncate text-sm font-semibold uppercase tracking-wide text-muted-foreground"
        >
          Places to discover
        </h2>
        <Link to="/explore" className="shrink-0 text-sm font-medium text-primary hover:underline">
          Explore nearby
        </Link>
      </div>

      {isLoading ? (
        <CardSkeletonGrid count={3} />
      ) : error ? (
        <ErrorMessage compact message={error} onRetry={onRetry} />
      ) : places.length === 0 ? (
        <EmptyState
          icon={Compass}
          title="No nearby places found"
          description="We couldn''t find travel-worthy places nearby. Try exploring a different area."
          action={
            <Link to="/explore">
              <Button variant="outline">Open Explore</Button>
            </Link>
          }
        />
      ) : (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {places.map((place) => (
            <article
              key={place.placeId}
              className="group overflow-hidden rounded-3xl border border-border bg-card transition-all duration-200 hover:-translate-y-0.5 hover:shadow-md"
            >
              <div className="p-5">
                <h3 className="font-semibold text-foreground line-clamp-1">{place.name}</h3>
                <p className="mt-1 text-sm text-muted-foreground line-clamp-2">{place.address}</p>
                {place.rating && (
                  <div className="mt-3 flex items-center gap-2">
                    <span className="text-xs font-medium text-foreground">⭐ {place.rating.toFixed(1)}</span>
                    {place.primaryType && (
                      <span className="text-xs text-muted-foreground">• {place.primaryType.replace(/_/g, " ")}</span>
                    )}
                  </div>
                )}
                {place.googleMapsUri && (
                  <a
                    href={place.googleMapsUri}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="mt-3 inline-block text-xs font-medium text-primary hover:underline"
                  >
                    View on Google Maps →
                  </a>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}
