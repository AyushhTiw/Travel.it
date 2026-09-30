import { createFileRoute } from "@tanstack/react-router";

import { DestinationsPage } from "@/pages/DestinationsPage";

export const Route = createFileRoute("/_authenticated/destinations/")({
  head: () => ({
    meta: [
      { title: "Destinations — Travel.it" },
      { name: "description", content: "Browse destinations you can plan a trip around." },
    ],
  }),
  component: DestinationsPage,
});
