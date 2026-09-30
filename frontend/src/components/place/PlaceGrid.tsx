import { MapPin } from "lucide-react";

import { CardSkeletonGrid } from "@/components/common/Loader";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { PlaceCard } from "./PlaceCard";
import type { Place } from "@/types/place";

export interface PlaceGridProps {
  places: Place[];
  isLoading: boolean;
  error: string | null;
  onRetry?: () => void;
  emptyTitle?: string;
  emptyDescription?: string;
}

export function PlaceGrid({
  places,
  isLoading,
  error,
  onRetry,
  emptyTitle = "No places to show",
  emptyDescription = "Try a different search or check back once more places are added.",
}: PlaceGridProps) {
  if (isLoading) return <CardSkeletonGrid />;
  if (error) return <ErrorMessage message={error} onRetry={onRetry} />;
  if (places.length === 0) {
    return <EmptyState icon={MapPin} title={emptyTitle} description={emptyDescription} />;
  }

  return (
    <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
      {places.map((place) => (
        <PlaceCard key={place.id} place={place} />
      ))}
    </div>
  );
}
