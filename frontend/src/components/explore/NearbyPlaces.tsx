import { Compass } from "lucide-react";

import { CardSkeletonGrid } from "@/components/common/Loader";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { NearbyPlaceCard } from "./NearbyPlaceCard";
import type { GooglePlace } from "@/types/places";

export interface NearbyPlacesProps {
  places: GooglePlace[] | null;
  isLoading: boolean;
  error: string | null;
  categoryLabel?: string;
  onRetry?: () => void;
}

export function NearbyPlaces({
  places,
  isLoading,
  error,
  categoryLabel = "Places",
  onRetry,
}: NearbyPlacesProps) {
  if (isLoading) return <CardSkeletonGrid count={6} />;
  if (error) return <ErrorMessage message={error} onRetry={onRetry} />;

  if (places === null) {
    return (
      <EmptyState
        icon={Compass}
        title="Ready when you are"
        description="Share your location, then pick a radius to discover places around you."
      />
    );
  }

  if (places.length === 0) {
    return (
      <EmptyState
        icon={Compass}
        title={`No ${categoryLabel} places found`}
        description="Try another category or widen the search radius."
      />
    );
  }

  return (
    <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
      {places.map((place) => (
        <NearbyPlaceCard key={place.placeId} place={place} />
      ))}
    </div>
  );
}
