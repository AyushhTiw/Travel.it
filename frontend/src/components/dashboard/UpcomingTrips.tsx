import { Link } from "@tanstack/react-router";
import { Plane } from "lucide-react";

import { Button } from "@/components/common/Button";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { ListSkeleton } from "@/components/common/Loader";
import { TripCard } from "@/components/trip/TripCard";
import { isUpcoming } from "@/utils/formatDate";
import type { Trip } from "@/types/trip";

export interface UpcomingTripsProps {
  trips: Trip[];
  isLoading: boolean;
  error: string | null;
  isPendingIntegration: boolean;
}

export function UpcomingTrips({ trips, isLoading, error, isPendingIntegration }: UpcomingTripsProps) {
  const upcoming = trips.filter((trip) => isUpcoming(trip.startDate)).slice(0, 3);

  return (
    <section aria-labelledby="upcoming-trips-heading">
      <div className="mb-4 grid grid-cols-[minmax(0,1fr)_auto] items-center gap-3">
        <h2
          id="upcoming-trips-heading"
          className="truncate text-sm font-semibold uppercase tracking-wide text-muted-foreground"
        >
          Upcoming trips
        </h2>
        <Link to="/trips" className="shrink-0 text-sm font-medium text-primary hover:underline">
          View all
        </Link>
      </div>

      {isPendingIntegration ? (
        <EmptyState
          icon={Plane}
          title="Trip planning is almost ready"
          description="Trip saving is still being connected to the backend. You can already draft a trip and it will sync once the service goes live."
          action={
            <Link to="/trips/create">
              <Button variant="outline">Draft a trip</Button>
            </Link>
          }
        />
      ) : isLoading ? (
        <ListSkeleton rows={2} />
      ) : error ? (
        <ErrorMessage compact message={error} />
      ) : upcoming.length === 0 ? (
        <EmptyState
          icon={Plane}
          title="No upcoming trips"
          description="Plan your next escape and it will show up right here."
          action={
            <Link to="/trips/create">
              <Button>Create a trip</Button>
            </Link>
          }
        />
      ) : (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {upcoming.map((trip) => (
            <TripCard key={trip.id} trip={trip} />
          ))}
        </div>
      )}
    </section>
  );
}
