import { createFileRoute } from "@tanstack/react-router";

import { DashboardPage } from "@/pages/DashboardPage";

export const Route = createFileRoute("/_authenticated/dashboard")({
  head: () => ({
    meta: [
      { title: "Dashboard — Travel.it" },
      { name: "description", content: "Your trips, nearby places and budget at a glance." },
    ],
  }),
  component: DashboardPage,
});
