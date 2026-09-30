import { Link } from "@tanstack/react-router";
import { Sparkles } from "lucide-react";
import { useState } from "react";

import { APP_NAV_ITEMS } from "./navItems";
import { Trevvy } from "@/components/ai/Trevvy";

export function Sidebar() {
  const [isTrevvyOpen, setIsTrevvyOpen] = useState(false);

  return (
    <>
      <aside className="hidden w-60 shrink-0 lg:block" aria-label="Section navigation">
        <nav className="sticky top-24 space-y-1">
          {APP_NAV_ITEMS.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              activeProps={{ className: "bg-card text-foreground shadow-sm" }}
              className="flex items-center gap-3 rounded-2xl px-4 py-2.5 text-sm font-medium text-muted-foreground transition-colors hover:bg-card hover:text-foreground"
            >
              <item.icon className="size-4 text-primary" aria-hidden />
              {item.label}
            </Link>
          ))}

          <button
            onClick={() => setIsTrevvyOpen(true)}
            className="group relative mt-6 w-full overflow-hidden rounded-3xl bg-gradient-to-br from-ink via-ink to-ink/80 p-5 text-left text-ink-foreground transition-all duration-300 hover:scale-[1.02] hover:shadow-2xl active:scale-[0.98]"
          >
            {/* Animated gradient overlay */}
            <div className="absolute inset-0 bg-gradient-to-r from-accent/20 via-transparent to-accent/20 opacity-0 transition-opacity duration-500 group-hover:opacity-100" />
            
            {/* Floating particles effect */}
            <div className="absolute -right-4 -top-4 size-24 rounded-full bg-accent/10 blur-2xl transition-all duration-700 group-hover:scale-150" />
            <div className="absolute -bottom-4 -left-4 size-20 rounded-full bg-primary/10 blur-2xl transition-all duration-700 group-hover:scale-150" />
            
            <div className="relative">
              <div className="flex items-center gap-2">
                <img
                  src="/trevvy-arrow.png"
                  alt=""
                  aria-hidden
                  className="size-8 object-contain transition-all duration-700 ease-out group-hover:-translate-y-3 group-hover:translate-x-3"
                />
              </div>
              <p className="mt-3 font-display text-base font-semibold transition-colors duration-300 group-hover:text-accent">
                Trevvy AI
              </p>
              <p className="mt-1 text-xs text-ink-foreground/70 transition-colors duration-300 group-hover:text-ink-foreground/90">
                Your personal travel companion. Click to chat!
              </p>
            </div>
            
            {/* Pulse animation on hover */}
            <div className="absolute inset-0 rounded-3xl ring-2 ring-accent/0 transition-all duration-300 group-hover:ring-accent/30 group-hover:ring-4" />
          </button>
        </nav>
      </aside>

      <Trevvy isOpen={isTrevvyOpen} onClose={() => setIsTrevvyOpen(false)} />
    </>
  );
}
