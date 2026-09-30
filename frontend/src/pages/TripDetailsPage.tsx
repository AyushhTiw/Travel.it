import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Skeleton } from "@/components/common/Loader";
import { AppLayout } from "@/components/layout/AppLayout";
import { TripHeader } from "@/components/trip/TripHeader";
import { TripTimeline } from "@/components/trip/TripTimeline";
import { useTrip } from "@/hooks/useTrips";

export function TripDetailsPage({ tripId }: { tripId: string }) {
  const { trip, isLoading, error, reload } = useTrip(tripId);

  return (
    <AppLayout>
      {isLoading ? (
        <div className="space-y-6">
          <Skeleton className="h-48 w-full" />
          <Skeleton className="h-6 w-1/3" />
        </div>
      ) : error || !trip ? (
        <ErrorMessage message={error ?? "Unable to load this trip."} onRetry={reload} />
      ) : (
        <div className="space-y-8">
          <TripHeader trip={trip} />
          <TripTimeline items={trip.itinerary ?? []} />
        </div>
      )}
    </AppLayout>
  );
}
