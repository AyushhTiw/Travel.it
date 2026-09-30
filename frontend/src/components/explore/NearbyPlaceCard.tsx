import { MapPin, Star, ExternalLink } from "lucide-react";
import type { GooglePlace } from "@/types/places";

// Human-readable label from Google primaryType
function formatType(type: string | null): string {
  if (!type) return "Place";
  return type
    .replace(/_/g, " ")
    .replace(/\b\w/g, (c) => c.toUpperCase());
}

export function NearbyPlaceCard({ place }: { place: GooglePlace }) {
  const mapsUrl = `https://www.google.com/maps/place/?q=place_id:${place.placeId}`;

  return (
    <a
      href={mapsUrl}
      target="_blank"
      rel="noopener noreferrer"
      className="group flex flex-col rounded-3xl border border-border bg-card p-5 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg hover:border-primary/30"
    >
      <div className="flex items-start justify-between gap-2">
        <h3 className="line-clamp-2 text-base font-semibold text-foreground leading-tight">
          {place.name}
        </h3>
        <ExternalLink className="mt-0.5 size-3.5 shrink-0 text-muted-foreground opacity-0 transition-opacity group-hover:opacity-100" aria-hidden />
      </div>

      {place.primaryType && (
        <span className="mt-2 inline-block self-start rounded-full bg-primary/10 px-2.5 py-0.5 text-[11px] font-medium text-primary">
          {formatType(place.primaryType)}
        </span>
      )}

      {place.address && (
        <p className="mt-2.5 flex min-w-0 items-start gap-1.5 text-xs text-muted-foreground">
          <MapPin className="mt-0.5 size-3 shrink-0 text-primary" aria-hidden />
          <span className="line-clamp-2">{place.address}</span>
        </p>
      )}

      {place.rating != null && (
        <div className="mt-3 flex items-center gap-1">
          <Star className="size-3.5 fill-amber-400 text-amber-400" aria-hidden />
          <span className="text-xs font-semibold text-foreground">{place.rating.toFixed(1)}</span>
        </div>
      )}
    </a>
  );
}
