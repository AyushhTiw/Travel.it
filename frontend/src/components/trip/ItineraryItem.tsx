import { Clock, MapPin } from "lucide-react";

import type { ItineraryItem as ItineraryItemType } from "@/types/trip";

export function ItineraryItem({ item }: { item: ItineraryItemType }) {
  return (
    <li className="relative pl-8">
      <span className="absolute left-0 top-1.5 grid size-5 place-items-center rounded-full border-2 border-primary bg-background">
        <span className="size-1.5 rounded-full bg-primary" aria-hidden />
      </span>
      <div className="rounded-3xl border border-border bg-card p-5">
        <div className="grid grid-cols-[minmax(0,1fr)_auto] items-start gap-3">
          <h3 className="truncate text-base font-semibold text-foreground">{item.title}</h3>
          {item.startTime ? (
            <span className="flex shrink-0 items-center gap-1 text-xs text-muted-foreground">
              <Clock className="size-3.5" aria-hidden />
              {item.startTime}
            </span>
          ) : null}
        </div>
        {item.description ? <p className="mt-2 text-sm text-muted-foreground">{item.description}</p> : null}
        {item.placeId ? (
          <p className="mt-3 flex items-center gap-1.5 text-xs text-muted-foreground">
            <MapPin className="size-3.5 text-primary" aria-hidden />
            Linked place #{item.placeId}
          </p>
        ) : null}
      </div>
    </li>
  );
}
