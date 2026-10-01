import { useState } from "react";
import { useNavigate, useParams } from "@tanstack/react-router";
import { ArrowLeft } from "lucide-react";

import { AppLayout } from "@/components/layout/AppLayout";
import { TripForm } from "@/components/trip/TripForm";
import { Button } from "@/components/common/Button";
import { ListSkeleton } from "@/components/common/Loader";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { tripService } from "@/services/tripService";
import { toFriendlyMessage } from "@/services/api";
import { useEffect } from "react";
import type { Trip, TripPayload } from "@/types/trip";

export function EditTripPage() {
  const { tripId } = useParams({ from: "/trips/$tripId/edit" });
  const navigate = useNavigate();
  
  const [trip, setTrip] = useState<Trip | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTrip = async () => {
      try {
        const data = await tripService.getById(tripId);
        setTrip(data);
      } catch (err) {
        setLoadError(toFriendlyMessage(err, "Failed to load trip"));
      } finally {
        setIsLoading(false);
      }
    };
    void fetchTrip();
  }, [tripId]);

  const handleSubmit = async (payload: TripPayload) => {
    setIsSubmitting(true);
    setSubmitError(null);
    try {
      await tripService.update(tripId, payload);
      navigate({ to: "/trips/$tripId", params: { tripId } });
    } catch (err) {
      setSubmitError(toFriendlyMessage(err, "Failed to update trip"));
      setIsSubmitting(false);
    }
  };

  const handleBack = () => navigate({ to: "/trips" });

  return (
    <AppLayout
      title="Edit trip"
      description="Update your trip details and dates."
      actions={
        <Button
          variant="ghost"
          leftIcon={<ArrowLeft className="size-4" aria-hidden />}
          onClick={handleBack}
        >
          Back
        </Button>
      }
    >
      <div className="mx-auto max-w-2xl">
        {isLoading ? (
          <ListSkeleton rows={5} />
        ) : loadError ? (
          <ErrorMessage message={loadError} onRetry={() => window.location.reload()} />
        ) : trip ? (
          <TripForm
            onSubmit={handleSubmit}
            isSubmitting={isSubmitting}
            submitError={submitError}
            submitLabel="Save changes"
            initialValues={{
              title: trip.title,
              destination: trip.destination,
              startDate: trip.startDate,
              endDate: trip.endDate,
              description: trip.description,
            }}
          />
        ) : null}
      </div>
    </AppLayout>
  );
}