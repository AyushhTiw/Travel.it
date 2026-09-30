import { createFileRoute } from "@tanstack/react-router";

import { TripsPage } from "@/pages/TripsPage";

export const Route = createFileRoute("/_authenticated/trips/")({
  head: () => ({
    meta: [
      { title: "Trips — Travel.it" },
      { name: "description", content: "Your plans, dates and day-by-day itineraries." },
    ],
  }),
  component: TripsPage,
});
