import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Skeleton } from "@/components/common/Loader";
import { AppLayout } from "@/components/layout/AppLayout";
import { PlaceDetails } from "@/components/place/PlaceDetails";
import { usePlace } from "@/hooks/usePlaces";

export function PlaceDetailsPage({ placeId }: { placeId: string }) {
  const { place, isLoading, error, reload } = usePlace(placeId);

  return (
    <AppLayout>
      {isLoading ? (
        <div className="space-y-6">
          <Skeleton className="h-48 w-full" />
          <Skeleton className="h-6 w-1/3" />
          <Skeleton className="h-40 w-full" />
        </div>
      ) : error || !place ? (
        <ErrorMessage message={error ?? "Unable to load this place."} onRetry={reload} />
      ) : (
        <PlaceDetails place={place} />
      )}
    </AppLayout>
  );
}
