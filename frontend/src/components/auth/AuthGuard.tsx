import { useEffect, type ReactNode } from "react";
import { useNavigate } from "@tanstack/react-router";

import { Loader } from "@/components/common/Loader";
import { useAuth } from "@/hooks/useAuth";

/**
 * Protects pages that REQUIRE login (trips, budget, profile, dashboard).
 * While auth is initialising the protected content is hidden.
 * Once loaded, unauthenticated users are redirected to /login.
 */
export function AuthGuard({ children }: { children: ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      void navigate({ to: "/login", replace: true });
    }
  }, [isAuthenticated, isLoading, navigate]);

  if (isLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-background">
        <Loader label="Getting things ready" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-background">
        <Loader label="Redirecting to login" />
      </div>
    );
  }

  return <>{children}</>;
}

/**
 * Keeps signed-in travellers away from /login and /signup.
 */
export function GuestGuard({ children }: { children: ReactNode }) {
  const { isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoading && isAuthenticated) {
      void navigate({ to: "/dashboard", replace: true });
    }
  }, [isAuthenticated, isLoading, navigate]);

  if (isLoading || isAuthenticated) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-background">
        <Loader label="Loading" />
      </div>
    );
  }

  return <>{children}</>;
}

/**
 * Soft guard — renders content for BOTH guests and logged-in users.
 * Does NOT redirect guests. Used for public pages that are better
 * when authenticated (e.g. Explore, Destinations) but fully usable as guest.
 *
 * isLoading is handled gracefully: content renders after auth state resolves,
 * but the page does NOT block on auth completion.
 */
export function PublicGuard({ children }: { children: ReactNode }) {
  // No redirect, no block. Auth context is available via useAuth() inside children.
  return <>{children}</>;
}
