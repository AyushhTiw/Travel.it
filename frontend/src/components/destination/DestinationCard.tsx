import { Link } from "@tanstack/react-router";
import { ArrowUpRight, MapPin } from "lucide-react";

import type { Destination } from "@/types/destination";

export function DestinationCard({ destination }: { destination: Destination }) {
  return (
    <Link
      to="/destinations/$destinationId"
      params={{ destinationId: String(destination.id) }}
      className="group flex flex-col rounded-3xl border border-border bg-card p-6 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg"
    >
      <div className="grid grid-cols-[minmax(0,1fr)_auto] items-start gap-3">
        <div className="min-w-0">
          <h3 className="truncate text-lg font-semibold text-foreground">{destination.name}</h3>
          <p className="mt-1 flex min-w-0 items-center gap-1.5 text-sm text-muted-foreground">
            <MapPin className="size-3.5 shrink-0 text-primary" aria-hidden />
            <span className="truncate">
              {[destination.state, destination.country].filter(Boolean).join(", ") || "Location coming soon"}
            </span>
          </p>
        </div>
        <span className="grid size-9 shrink-0 place-items-center rounded-full bg-secondary text-primary transition-colors group-hover:bg-primary group-hover:text-primary-foreground">
          <ArrowUpRight className="size-4" aria-hidden />
        </span>
      </div>
      {destination.description ? (
        <p className="mt-4 line-clamp-3 text-sm text-muted-foreground">{destination.description}</p>
      ) : null}
    </Link>
  );
}
