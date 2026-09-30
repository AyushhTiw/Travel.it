import { createFileRoute } from "@tanstack/react-router";

import { PlaceDetailsPage } from "@/pages/PlaceDetailsPage";

export const Route = createFileRoute("/_authenticated/places/$placeId")({
  head: () => ({
    meta: [
      { title: "Place — Travel.it" },
      { name: "description", content: "Place details, location and travel notes." },
    ],
  }),
  component: PlaceDetailsRoute,
});

function PlaceDetailsRoute() {
  const { placeId } = Route.useParams();
  return <PlaceDetailsPage placeId={placeId} />;
}
