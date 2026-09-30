import { Link } from "@tanstack/react-router";
import { Compass, MapPin } from "lucide-react";

import { Button } from "@/components/common/Button";
import type { Destination } from "@/types/destination";

export function DestinationHeader({ destination }: { destination: Destination }) {
  const location = [destination.state, destination.country].filter(Boolean).join(", ");

  return (
    <section className="overflow-hidden rounded-3xl bg-ink px-6 py-10 text-ink-foreground sm:px-10">
      <p className="flex items-center gap-1.5 text-xs font-semibold uppercase tracking-wide text-accent">
        <MapPin className="size-3.5" aria-hidden />
        {location || "Destination"}
      </p>
      <h1 className="mt-3 text-3xl font-semibold sm:text-4xl">{destination.name}</h1>
      {destination.description ? (
        <p className="mt-4 max-w-2xl text-sm text-ink-foreground/75 sm:text-base">{destination.description}</p>
      ) : null}
      <div className="mt-7 flex flex-wrap gap-3">
        <Link to="/explore">
          <Button leftIcon={<Compass className="size-4" aria-hidden />}>Explore nearby</Button>
        </Link>
        <Link to="/places">
          <Button variant="outline" className="border-ink-foreground/25 bg-transparent text-ink-foreground hover:bg-ink-foreground/10">
            Browse places
          </Button>
        </Link>
      </div>
    </section>
  );
}
