import { Link } from "@tanstack/react-router";
import { Compass, Home } from "lucide-react";

import { Button } from "@/components/common/Button";

export function NotFoundPage() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-background px-4 py-16 text-center">
      <img src="/travel-it-logo.png" alt="" aria-hidden className="w-28 object-contain opacity-80" />
      <p className="mt-8 font-display text-6xl font-semibold text-primary/25">404</p>
      <h1 className="mt-4 text-2xl font-semibold text-foreground sm:text-3xl">This route isn't on the map</h1>
      <p className="mt-3 max-w-md text-sm text-muted-foreground">
        The page you're looking for has moved or never existed. Let's get you back on track.
      </p>
      <div className="mt-8 flex flex-wrap justify-center gap-3">
        <Link to="/">
          <Button leftIcon={<Home className="size-4" aria-hidden />}>Go home</Button>
        </Link>
        <Link to="/explore">
          <Button variant="outline" leftIcon={<Compass className="size-4" aria-hidden />}>
            Explore places
          </Button>
        </Link>
      </div>
    </div>
  );
}
