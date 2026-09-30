import { Compass, Globe2, MapPin, Navigation } from "lucide-react";

import type { Place } from "@/types/place";

export function PlaceDetails({ place }: { place: Place }) {
  const facts = [
    { icon: MapPin, label: "City", value: [place.city, place.state].filter(Boolean).join(", ") || "—" },
    { icon: Globe2, label: "Country", value: place.country || "—" },
    { icon: Navigation, label: "Address", value: place.address || "—" },
    {
      icon: Compass,
      label: "Coordinates",
      value:
        Number.isFinite(place.latitude) && Number.isFinite(place.longitude)
          ? `${place.latitude.toFixed(4)}, ${place.longitude.toFixed(4)}`
          : "—",
    },
  ];

  return (
    <article className="space-y-8">
      <header className="rounded-3xl bg-ink px-6 py-10 text-ink-foreground sm:px-10">
        <p className="text-xs font-semibold uppercase tracking-wide text-accent">Place</p>
        <h1 className="mt-3 text-3xl font-semibold sm:text-4xl">{place.name}</h1>
        <p className="mt-3 text-sm text-ink-foreground/75">
          {[place.city, place.state, place.country].filter(Boolean).join(" · ")}
        </p>
      </header>

      {place.description ? (
        <section className="rounded-3xl border border-border bg-card p-6 sm:p-8">
          <h2 className="text-lg font-semibold text-foreground">About this place</h2>
          <p className="mt-3 text-sm leading-relaxed text-muted-foreground">{place.description}</p>
        </section>
      ) : null}

      <section className="grid gap-4 sm:grid-cols-2">
        {facts.map((fact) => (
          <div key={fact.label} className="flex min-w-0 items-start gap-3 rounded-3xl border border-border bg-card p-5">
            <span className="grid size-10 shrink-0 place-items-center rounded-2xl bg-secondary text-primary">
              <fact.icon className="size-4" aria-hidden />
            </span>
            <div className="min-w-0">
              <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">{fact.label}</p>
              <p className="mt-1 break-words text-sm text-foreground">{fact.value}</p>
            </div>
          </div>
        ))}
      </section>
    </article>
  );
}
