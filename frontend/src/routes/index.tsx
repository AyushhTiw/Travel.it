import { createFileRoute } from "@tanstack/react-router";

import { LandingPage } from "@/pages/LandingPage";

export const Route = createFileRoute("/")({
  ssr: false,
  head: () => ({
    meta: [
      { title: "Travel.it — Plan less. Experience more." },
      {
        name: "description",
        content:
          "Travel.it is your AI travel buddy for discovering places, planning trips and tracking budgets in one calm workspace.",
      },
      { property: "og:title", content: "Travel.it — Plan less. Experience more." },
      {
        property: "og:description",
        content: "Your intelligent travel companion for discovering places, planning trips, and traveling smarter.",
      },
    ],
  }),
  component: LandingPage,
});
