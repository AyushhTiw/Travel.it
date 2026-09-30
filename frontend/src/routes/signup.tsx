import { createFileRoute } from "@tanstack/react-router";

import { GuestGuard } from "@/components/auth/AuthGuard";
import { SignupPage } from "@/pages/SignupPage";

export const Route = createFileRoute("/signup")({
  ssr: false,
  head: () => ({
    meta: [
      { title: "Create your account — Travel.it" },
      { name: "description", content: "Sign up for Travel.it and start planning trips that feel like yours." },
      { property: "og:title", content: "Create your account — Travel.it" },
      { property: "og:description", content: "Sign up for Travel.it and start planning trips that feel like yours." },
    ],
  }),
  component: () => (
    <GuestGuard>
      <SignupPage />
    </GuestGuard>
  ),
});
