import { Link } from "@tanstack/react-router";
import { Compass } from "lucide-react";

import { Button } from "@/components/common/Button";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Skeleton } from "@/components/common/Loader";
import { AppLayout } from "@/components/layout/AppLayout";
import { DestinationHeader } from "@/components/destination/DestinationHeader";
import { PlaceGrid } from "@/components/place/PlaceGrid";
import { useDestination } from "@/hooks/useDestinations";
import { usePlaces } from "@/hooks/usePlaces";

export function DestinationDetailsPage({ destinationId }: { destinationId: string }) {
  const { destination, isLoading, error, reload } = useDestination(destinationId);
  const places = usePlaces(destination?.country ? { country: destination.country } : {});

  return (
    <AppLayout>
      {isLoading ? (
        <div className="space-y-6">
          <Skeleton className="h-48 w-full" />
          <Skeleton className="h-6 w-1/3" />
          <Skeleton className="h-40 w-full" />
        </div>
      ) : error || !destination ? (
        <ErrorMessage message={error ?? "Unable to load this destination."} onRetry={reload} />
      ) : (
        <div className="space-y-10">
          <DestinationHeader destination={destination} />

          <section>
            <h2 className="text-sm font-semibold uppercase tracking-wide text-muted-foreground">About</h2>
            <p className="mt-3 max-w-3xl text-sm leading-relaxed text-muted-foreground">
              {destination.description || "No description has been added for this destination yet."}
            </p>
            <div className="mt-6 flex flex-wrap gap-3">
              <Link to="/explore">
                <Button leftIcon={<Compass className="size-4" aria-hidden />}>Explore nearby</Button>
              </Link>
              <Link to="/trips/create">
                <Button variant="outline">Plan a trip here</Button>
              </Link>
            </div>
          </section>

          <section>
            <h2 className="mb-4 text-sm font-semibold uppercase tracking-wide text-muted-foreground">
              Places in {destination.country}
            </h2>
            <PlaceGrid
              places={places.places}
              isLoading={places.isLoading}
              error={places.error}
              onRetry={places.reload}
              emptyTitle="No places listed yet"
              emptyDescription="Places for this destination will appear here once they're added."
            />
          </section>
        </div>
      )}
    </AppLayout>
  );
}
