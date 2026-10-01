import { useEffect, useState } from "react";
import { Sparkles } from "lucide-react";

import type { User } from "@/types/auth";

function getTimeBasedGreeting(): string {
  const hour = new Date().getHours();
  
  // 05:00?11:59 ? GOOD MORNING
  if (hour >= 5 && hour < 12) {
    return "GOOD MORNING";
  }
  // 12:00?16:59 ? GOOD AFTERNOON
  if (hour >= 12 && hour < 17) {
    return "GOOD AFTERNOON";
  }
  // 17:00?20:59 ? GOOD EVENING
  if (hour >= 17 && hour < 21) {
    return "GOOD EVENING";
  }
  // 21:00?04:59 ? GOOD NIGHT
  return "GOOD NIGHT";
}

function checkFirstVisit(userId: number): boolean {
  const key = `travelit_dashboard_visited_${userId}`;
  const hasVisited = localStorage.getItem(key);
  
  if (!hasVisited) {
    localStorage.setItem(key, "true");
    return true;
  }
  
  return false;
}

export function WelcomeSection({ user }: { user: User }) {
  const [isFirstVisit, setIsFirstVisit] = useState(false);
  const greeting = getTimeBasedGreeting();

  useEffect(() => {
    // Check if this is the first visit for this specific user
    const firstVisit = checkFirstVisit(user.userId);
    setIsFirstVisit(firstVisit);
  }, [user.userId]);

  const welcomeMessage = isFirstVisit 
    ? `Welcome, ${user.name}`
    : `Welcome back, ${user.name}`;

  return (
    <section className="overflow-hidden rounded-3xl bg-ink px-6 py-9 text-ink-foreground sm:px-10">
      <p className="flex items-center gap-1.5 text-xs font-semibold uppercase tracking-wide text-accent">
        <Sparkles className="size-3.5" aria-hidden />
        {greeting}
      </p>
      <h1 className="mt-3 text-2xl font-semibold sm:text-3xl">{welcomeMessage}</h1>
      <p className="mt-3 max-w-xl text-sm text-ink-foreground/75">
        Plan less. Experience more. Here's everything you need for your next journey.
      </p>
    </section>
  );
}
