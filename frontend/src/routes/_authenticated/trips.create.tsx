import { createFileRoute } from "@tanstack/react-router";

import { CreateTripPage } from "@/pages/CreateTripPage";

export const Route = createFileRoute("/_authenticated/trips/create")({
  head: () => ({
    meta: [
      { title: "Create a trip — Travel.it" },
      { name: "description", content: "Name your trip, set the dates and build the timeline." },
    ],
  }),
  component: CreateTripPage,
});
