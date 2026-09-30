import { Map } from "lucide-react";

import { CardSkeletonGrid } from "@/components/common/Loader";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { DestinationCard } from "./DestinationCard";
import type { Destination } from "@/types/destination";

export interface DestinationGridProps {
  destinations: Destination[];
  isLoading: boolean;
  error: string | null;
  onRetry?: () => void;
  emptyTitle?: string;
  emptyDescription?: string;
}

export function DestinationGrid({
  destinations,
  isLoading,
  error,
  onRetry,
  emptyTitle = "No destinations yet",
  emptyDescription = "Once destinations are added to the catalogue they'll show up here.",
}: DestinationGridProps) {
  if (isLoading) return <CardSkeletonGrid />;
  if (error) return <ErrorMessage message={error} onRetry={onRetry} />;
  if (destinations.length === 0) {
    return <EmptyState icon={Map} title={emptyTitle} description={emptyDescription} />;
  }

  return (
    <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-3">
      {destinations.map((destination) => (
        <DestinationCard key={destination.id} destination={destination} />
      ))}
    </div>
  );
}
