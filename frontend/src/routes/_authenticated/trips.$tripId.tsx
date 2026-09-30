import { createFileRoute } from "@tanstack/react-router";

import { TripDetailsPage } from "@/pages/TripDetailsPage";

export const Route = createFileRoute("/_authenticated/trips/$tripId")({
  head: () => ({
    meta: [
      { title: "Trip — Travel.it" },
      { name: "description", content: "Trip details and day-by-day itinerary." },
    ],
  }),
  component: TripDetailsRoute,
});

function TripDetailsRoute() {
  const { tripId } = Route.useParams();
  return <TripDetailsPage tripId={tripId} />;
}
