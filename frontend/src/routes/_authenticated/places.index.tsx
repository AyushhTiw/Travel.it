import { createFileRoute } from "@tanstack/react-router";

import { PlacesPage } from "@/pages/PlacesPage";

export const Route = createFileRoute("/_authenticated/places/")({
  head: () => ({
    meta: [
      { title: "Places — Travel.it" },
      { name: "description", content: "Every place Travel.it knows about, ready for your itinerary." },
    ],
  }),
  component: PlacesPage,
});
