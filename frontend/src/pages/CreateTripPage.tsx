import { useNavigate } from "@tanstack/react-router";
import { AppLayout } from "@/components/layout/AppLayout";
import { TripForm } from "@/components/trip/TripForm";
import { useCreateTrip } from "@/hooks/useTrips";

export function CreateTripPage() {
  const navigate = useNavigate();
  const { createTrip, isSubmitting, error } = useCreateTrip();

  return (
    <AppLayout title="Create a trip" description="Name it, set the dates and we will build the timeline around it.">
      <div className="max-w-2xl">
        <div className="rounded-3xl border border-border bg-card p-6 sm:p-8">
          <TripForm
            isSubmitting={isSubmitting}
            submitError={error}
            submitLabel={isSubmitting ? "Saving trip..." : "Save trip"}
            onSubmit={(payload) => {
              void createTrip(payload).then((trip) => {
                if (trip) void navigate({ to: "/trips/$tripId", params: { tripId: String(trip.id) } });
              });
            }}
          />
        </div>
      </div>
    </AppLayout>
  );
}
