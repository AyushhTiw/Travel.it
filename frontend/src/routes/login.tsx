import { createFileRoute } from "@tanstack/react-router";

import { GuestGuard } from "@/components/auth/AuthGuard";
import { LoginPage } from "@/pages/LoginPage";

export const Route = createFileRoute("/login")({
  ssr: false,
  head: () => ({
    meta: [
      { title: "Log in — Travel.it" },
      { name: "description", content: "Log in to Travel.it to continue planning your trips." },
      { property: "og:title", content: "Log in — Travel.it" },
      { property: "og:description", content: "Log in to Travel.it to continue planning your trips." },
    ],
  }),
  component: () => (
    <GuestGuard>
      <LoginPage />
    </GuestGuard>
  ),
});
