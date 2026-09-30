import { createFileRoute } from "@tanstack/react-router";

import { ProfilePage } from "@/pages/ProfilePage";

export const Route = createFileRoute("/_authenticated/profile")({
  head: () => ({
    meta: [
      { title: "Profile — Travel.it" },
      { name: "description", content: "Your Travel.it account details." },
    ],
  }),
  component: ProfilePage,
});
