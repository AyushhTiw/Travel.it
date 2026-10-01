import { Link } from "@tanstack/react-router";
import { Plane, Plus } from "lucide-react";
import { Button } from "@/components/common/Button";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { ListSkeleton } from "@/components/common/Loader";
import { AppLayout } from "@/components/layout/AppLayout";
import { TripCard } from "@/components/trip/TripCard";
import { useTrips } from "@/hooks/useTrips";

export function TripsPage() {
  const { trips, isLoading, error, reload } = useTrips();

  return (
    <AppLayout
      title="Trips"
      description="Your plans, dates and day-by-day itineraries in one place."
      actions={
        <Link to="/trips/create">
          <Button leftIcon={<Plus className="size-4" aria-hidden />}>New trip</Button>
        </Link>
      }
    >
      {isLoading ? (
        <ListSkeleton rows={3} />
      ) : error ? (
        <ErrorMessage message={error} onRetry={reload} />
      ) : trips.length === 0 ? (
        <EmptyState
          icon={Plane}
          title="No trips yet"
          description="Create your first trip and start shaping the days."
          action={
            <Link to="/trips/create">
              <Button>Create a trip</Button>
            </Link>
          }
        />
      ) : (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {trips.map((trip) => (
            <TripCard key={trip.id} trip={trip} onDelete={reload} />
          ))}
        </div>
      )}
    </AppLayout>
  );
}