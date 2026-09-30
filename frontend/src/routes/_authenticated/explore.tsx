import { createFileRoute } from "@tanstack/react-router";

import { ExplorePage } from "@/pages/ExplorePage";

export const Route = createFileRoute("/_authenticated/explore")({
  head: () => ({
    meta: [
      { title: "Explore — Travel.it" },
      { name: "description", content: "Discover places around you within the radius you choose." },
    ],
  }),
  component: ExplorePage,
});
