import { Sparkles } from "lucide-react";

import type { User } from "@/types/auth";

export function WelcomeSection({ user }: { user: User }) {
  const hour = new Date().getHours();
  const greeting = hour < 12 ? "Good morning" : hour < 18 ? "Good afternoon" : "Good evening";

  return (
    <section className="overflow-hidden rounded-3xl bg-ink px-6 py-9 text-ink-foreground sm:px-10">
      <p className="flex items-center gap-1.5 text-xs font-semibold uppercase tracking-wide text-accent">
        <Sparkles className="size-3.5" aria-hidden />
        {greeting}
      </p>
      <h1 className="mt-3 text-2xl font-semibold sm:text-3xl">Welcome back, {user.name}</h1>
      <p className="mt-3 max-w-xl text-sm text-ink-foreground/75">
        Plan less. Experience more. Here's everything you need for your next journey.
      </p>
    </section>
  );
}
