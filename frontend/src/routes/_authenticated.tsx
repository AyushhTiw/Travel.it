import { Outlet, createFileRoute, useRouterState } from "@tanstack/react-router";
import { AuthGuard } from "@/components/auth/AuthGuard";
import { PublicGuard } from "@/components/auth/AuthGuard";

/**
 * Layout route wrapping all app pages.
 *
 * Pages under /_authenticated are split into two tiers:
 *
 * PUBLIC (guests welcome — no login required):
 *   /explore, /destinations/*, /places/*
 *
 * PROTECTED (login required):
 *   /dashboard, /trips/*, /budget, /profile
 *
 * The correct guard is selected based on the current path.
 */
const PUBLIC_PREFIXES = ["/explore", "/destinations", "/places"];

function SmartGuard() {
  const pathname = useRouterState({ select: (s) => s.location.pathname });
  const isPublicPath = PUBLIC_PREFIXES.some((prefix) => pathname.startsWith(prefix));

  if (isPublicPath) {
    return (
      <PublicGuard>
        <Outlet />
      </PublicGuard>
    );
  }

  return (
    <AuthGuard>
      <Outlet />
    </AuthGuard>
  );
}

export const Route = createFileRoute("/_authenticated")({
  ssr: false,
  component: SmartGuard,
});
