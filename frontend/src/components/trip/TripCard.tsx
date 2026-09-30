import { Link } from "@tanstack/react-router";
import { CalendarDays, MapPin } from "lucide-react";

import { formatDateRange } from "@/utils/formatDate";
import type { Trip } from "@/types/trip";

export function TripCard({ trip }: { trip: Trip }) {
  return (
    <Link
      to="/trips/$tripId"
      params={{ tripId: String(trip.id) }}
      className="group flex flex-col rounded-3xl border border-border bg-card p-6 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg"
    >
      <h3 className="truncate text-lg font-semibold text-foreground">{trip.title}</h3>
      <p className="mt-1.5 flex min-w-0 items-center gap-1.5 text-sm text-muted-foreground">
        <MapPin className="size-3.5 shrink-0 text-primary" aria-hidden />
        <span className="truncate">{trip.destination}</span>
      </p>
      <p className="mt-3 flex items-center gap-1.5 text-sm text-muted-foreground">
        <CalendarDays className="size-3.5 shrink-0 text-primary" aria-hidden />
        {formatDateRange(trip.startDate, trip.endDate)}
      </p>
      {trip.description ? (
        <p className="mt-4 line-clamp-2 text-sm text-muted-foreground">{trip.description}</p>
      ) : null}
    </Link>
  );
}
