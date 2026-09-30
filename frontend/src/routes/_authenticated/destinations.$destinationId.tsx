import { createFileRoute } from "@tanstack/react-router";

import { DestinationDetailsPage } from "@/pages/DestinationDetailsPage";

export const Route = createFileRoute("/_authenticated/destinations/$destinationId")({
  head: () => ({
    meta: [
      { title: "Destination — Travel.it" },
      { name: "description", content: "Destination details, places and planning actions." },
    ],
  }),
  component: DestinationDetailsRoute,
});

function DestinationDetailsRoute() {
  const { destinationId } = Route.useParams();
  return <DestinationDetailsPage destinationId={destinationId} />;
}
