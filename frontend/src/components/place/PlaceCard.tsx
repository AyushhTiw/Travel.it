import { Link } from "@tanstack/react-router";
import { MapPin } from "lucide-react";

import type { Place } from "@/types/place";

export function PlaceCard({ place }: { place: Place }) {
  return (
    <Link
      to="/places/$placeId"
      params={{ placeId: String(place.id) }}
      className="group flex flex-col rounded-3xl border border-border bg-card p-6 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg"
    >
      <div className="grid grid-cols-[minmax(0,1fr)_auto] items-start gap-3">
        <h3 className="truncate text-lg font-semibold text-foreground">{place.name}</h3>
        {place.active ? (
          <span className="shrink-0 rounded-full bg-success/12 px-2.5 py-1 text-[11px] font-semibold text-success">
            Open
          </span>
        ) : null}
      </div>
      <p className="mt-1.5 flex min-w-0 items-center gap-1.5 text-sm text-muted-foreground">
        <MapPin className="size-3.5 shrink-0 text-primary" aria-hidden />
        <span className="truncate">{[place.city, place.country].filter(Boolean).join(", ")}</span>
      </p>
      {place.description ? (
        <p className="mt-4 line-clamp-3 text-sm text-muted-foreground">{place.description}</p>
      ) : null}
    </Link>
  );
}
