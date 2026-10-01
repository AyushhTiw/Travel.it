import { CalendarDays, Edit2, MapPin } from "lucide-react";
import { Link } from "@tanstack/react-router";

import { daysBetween, formatDateRange } from "@/utils/formatDate";
import type { Trip } from "@/types/trip";

export function TripHeader({ trip }: { trip: Trip }) {
  const nights = daysBetween(trip.startDate, trip.endDate);

  return (
    <header className="rounded-3xl bg-ink px-6 py-10 text-ink-foreground sm:px-10 relative">
      <div className="flex items-start justify-between gap-4">
        <div className="flex-1 min-w-0">
          <p className="text-xs font-semibold uppercase tracking-wide text-accent">Trip</p>
          <h1 className="mt-3 text-3xl font-semibold sm:text-4xl">{trip.title}</h1>
          <div className="mt-5 flex flex-wrap gap-x-6 gap-y-2 text-sm text-ink-foreground/75">
            <span className="flex items-center gap-1.5">
              <MapPin className="size-4 shrink-0" aria-hidden />
              {trip.destination}
            </span>
            <span className="flex items-center gap-1.5">
              <CalendarDays className="size-4 shrink-0" aria-hidden />
              {formatDateRange(trip.startDate, trip.endDate)}
              {nights > 0 ? ` 🌙 ${nights} night${nights === 1 ? "" : "s"}` : ""}
            </span>
          </div>
          {trip.description ? (
            <p className="mt-5 max-w-2xl text-sm text-ink-foreground/75">{trip.description}</p>
          ) : null}
        </div>
        <Link
          to="/trips/$tripId/edit"
          params={{ tripId: String(trip.id) }}
          className="flex items-center gap-2 px-4 py-2 rounded-lg bg-ink-foreground/10 hover:bg-ink-foreground/20 text-ink-foreground transition-colors text-sm font-medium"
        >
          <Edit2 className="size-4" aria-hidden />
          Edit
        </Link>
      </div>
    </header>
  );
}