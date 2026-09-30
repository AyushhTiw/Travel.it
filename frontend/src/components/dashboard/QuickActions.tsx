import { Link } from "@tanstack/react-router";
import { Compass, MapPin, PiggyBank, Plus } from "lucide-react";
import type { LucideIcon } from "lucide-react";

interface QuickAction {
  to: "/explore" | "/trips/create" | "/places" | "/budget";
  label: string;
  hint: string;
  icon: LucideIcon;
}

const ACTIONS: readonly QuickAction[] = [
  { to: "/explore", label: "Explore", hint: "Discover around you", icon: Compass },
  { to: "/trips/create", label: "Create trip", hint: "Start planning", icon: Plus },
  { to: "/places", label: "Nearby places", hint: "Browse the map", icon: MapPin },
  { to: "/budget", label: "Budget", hint: "Track spending", icon: PiggyBank },
];

export function QuickActions() {
  return (
    <section aria-labelledby="quick-actions-heading">
      <h2 id="quick-actions-heading" className="mb-4 text-sm font-semibold uppercase tracking-wide text-muted-foreground">
        Quick actions
      </h2>
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        {ACTIONS.map((action) => (
          <Link
            key={action.to}
            to={action.to}
            className="group rounded-3xl border border-border bg-card p-5 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-md"
          >
            <span className="grid size-10 place-items-center rounded-2xl bg-secondary text-primary transition-colors group-hover:bg-primary group-hover:text-primary-foreground">
              <action.icon className="size-4" aria-hidden />
            </span>
            <p className="mt-4 truncate text-sm font-semibold text-foreground">{action.label}</p>
            <p className="mt-0.5 truncate text-xs text-muted-foreground">{action.hint}</p>
          </Link>
        ))}
      </div>
    </section>
  );
}
