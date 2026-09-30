import { CalendarDays, MapPin } from "lucide-react";

import { daysBetween, formatDateRange } from "@/utils/formatDate";
import type { Trip } from "@/types/trip";

export function TripHeader({ trip }: { trip: Trip }) {
  const nights = daysBetween(trip.startDate, trip.endDate);

  return (
    <header className="rounded-3xl bg-ink px-6 py-10 text-ink-foreground sm:px-10">
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
          {nights > 0 ? ` · ${nights} night${nights === 1 ? "" : "s"}` : ""}
        </span>
      </div>
      {trip.description ? <p className="mt-5 max-w-2xl text-sm text-ink-foreground/75">{trip.description}</p> : null}
    </header>
  );
}
