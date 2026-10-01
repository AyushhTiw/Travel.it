import { createFileRoute } from "@tanstack/react-router";

import { EditTripPage } from "@/pages/EditTripPage";

export const Route = createFileRoute("/_authenticated/trips/$tripId/edit")({
  head: () => ({
    meta: [
      { title: "Edit Trip — Travel.it" },
      { name: "description", content: "Update your trip details and dates." },
    ],
  }),
  component: EditTripRoute,
});

function EditTripRoute() {
  const { tripId } = Route.useParams();
  return <EditTripPage tripId={tripId} />;
}
