import { CalendarClock } from "lucide-react";

import { EmptyState } from "@/components/common/EmptyState";
import { ItineraryItem } from "./ItineraryItem";
import type { ItineraryItem as ItineraryItemType } from "@/types/trip";

export function TripTimeline({ items }: { items: ItineraryItemType[] }) {
  if (items.length === 0) {
    return (
      <EmptyState
        icon={CalendarClock}
        title="No itinerary yet"
        description="Itinerary items will appear here as a day-by-day timeline."
      />
    );
  }

  const days = [...new Set(items.map((item) => item.day))].sort((a, b) => a - b);

  return (
    <div className="space-y-8">
      {days.map((day) => (
        <section key={day}>
          <h2 className="mb-4 text-sm font-semibold uppercase tracking-wide text-muted-foreground">Day {day}</h2>
          <ol className="relative space-y-4 border-l border-border pl-0">
            {items
              .filter((item) => item.day === day)
              .map((item) => (
                <ItineraryItem key={item.id} item={item} />
              ))}
          </ol>
        </section>
      ))}
    </div>
  );
}
