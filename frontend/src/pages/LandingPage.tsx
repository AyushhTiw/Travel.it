import { useState } from "react";
import { Link } from "@tanstack/react-router";
import {
  ArrowRight,
  Compass,
  MapPin,
  PiggyBank,
  Route as RouteIcon,
  Search,
  Sparkles,
  Star,
} from "lucide-react";

import { Button } from "@/components/common/Button";
import { Logo } from "@/components/common/Logo";
import { Footer } from "@/components/layout/Footer";
import { Trevvy } from "@/components/ai/Trevvy";
import { useAuth } from "@/hooks/useAuth";

const FEATURES = [
  {
    icon: Search,
    title: "Search that understands you",
    body: "Find destinations, cities and hidden places without wading through endless lists.",
  },
  {
    icon: Compass,
    title: "Discover what''s nearby",
    body: "Pick a radius, share your location and see what''s worth the detour right now.",
  },
  {
    icon: RouteIcon,
    title: "Plan trips end to end",
    body: "Dates, notes and a day-by-day timeline, all in one calm workspace.",
  },
  {
    icon: PiggyBank,
    title: "Budgets that stay honest",
    body: "Set a total, add expenses by category and always know what''s left.",
  },
] as const;

const STEPS = [
  { step: "01", title: "Pick a destination", body: "Browse the catalogue or search by city and country." },
  { step: "02", title: "Build your days", body: "Add places, notes and timings into a clean itinerary." },
  { step: "03", title: "Travel smarter", body: "Track your budget as the trip unfolds — no spreadsheets." },
] as const;

export function LandingPage() {
  const { isAuthenticated } = useAuth();
  const [isTrevvyOpen, setIsTrevvyOpen] = useState(false);
  
  const primaryTo = isAuthenticated ? "/dashboard" : "/explore";
  const primaryLabel = isAuthenticated ? "Go to dashboard" : "Start planning free";

  return (
    <div className="min-h-screen bg-background">
      <header className="sticky top-0 z-40 border-b border-border/60 glass-panel">
        <nav
          aria-label="Main"
          className="mx-auto grid max-w-7xl grid-cols-[minmax(0,1fr)_auto] items-center gap-4 px-4 py-3 sm:px-6 lg:px-8"
        >
          <Link to="/" aria-label="Travel.it home" className="min-w-0">
            <Logo />
          </Link>
          <div className="flex shrink-0 items-center gap-2">
            {isAuthenticated ? (
              <Link to="/dashboard">
                <Button size="sm">Dashboard</Button>
              </Link>
            ) : (
              <>
                <Link to="/login">
                  <Button variant="ghost" size="sm">
                    Log in
                  </Button>
                </Link>
                <Link to="/signup">
                  <Button size="sm">Get started</Button>
                </Link>
              </>
            )}
          </div>
        </nav>
      </header>

      <main>
        {/* Hero */}
        <section className="relative overflow-hidden">
          <div
            aria-hidden
            className="pointer-events-none absolute inset-x-0 -top-40 h-96 bg-[radial-gradient(60%_60%_at_50%_50%,color-mix(in_oklab,var(--color-primary)_18%,transparent),transparent)]"
          />
          <div className="mx-auto grid max-w-7xl items-center gap-12 px-4 py-16 sm:px-6 lg:grid-cols-2 lg:px-8 lg:py-28">
            <div className="min-w-0">
              <h1 className="text-balance-tight text-4xl font-semibold leading-[1.05] text-foreground sm:text-6xl">
                Plan less.
                <br />
                Experience more.
              </h1>
              <p className="mt-6 max-w-lg text-base leading-relaxed text-muted-foreground sm:text-lg">
                Your intelligent travel companion for discovering places, planning trips, and traveling smarter.
              </p>
              <div className="mt-9 flex flex-wrap gap-3">
                <Link to={primaryTo}>
                  <Button size="lg" rightIcon={<ArrowRight className="size-4" aria-hidden />}>
                    {primaryLabel}
                  </Button>
                </Link>
                <Link to="/explore">
                  <Button size="lg" variant="outline">
                    Explore nearby
                  </Button>
                </Link>
              </div>
              <dl className="mt-12 grid max-w-md grid-cols-3 gap-6">
                {[
                  { label: "Curated destinations", value: "Global" },
                  { label: "Nearby discovery", value: "Live" },
                  { label: "Budget tracking", value: "Built in" },
                ].map((stat) => (
                  <div key={stat.label} className="min-w-0">
                    <dt className="truncate text-xs text-muted-foreground">{stat.label}</dt>
                    <dd className="mt-1 font-display text-lg font-semibold text-foreground">{stat.value}</dd>
                  </div>
                ))}
              </dl>
            </div>

            <div className="relative min-w-0">
              <div className="rounded-[2rem] border border-border bg-card p-8 shadow-xl">
                <img src="/travel-it-logo.png" alt="Travel.it" className="mx-auto w-56 object-contain" />
                <p className="mt-3 text-center font-wordmark text-sm font-light tracking-wide text-muted-foreground">
                  Your AI Travel Buddy
                </p>
                <div className="mt-8 space-y-3">
                  {[
                    { icon: MapPin, title: "Nearby places", hint: "Within your chosen radius" },
                    { icon: RouteIcon, title: "Trip timeline", hint: "Day-by-day planning" },
                    { icon: PiggyBank, title: "Budget health", hint: "Allocated vs remaining" },
                  ].map((row) => (
                    <div key={row.title} className="flex items-center gap-3 rounded-2xl bg-secondary px-4 py-3">
                      <span className="grid size-9 shrink-0 place-items-center rounded-xl bg-card text-primary">
                        <row.icon className="size-4" aria-hidden />
                      </span>
                      <div className="min-w-0">
                        <p className="truncate text-sm font-semibold text-foreground">{row.title}</p>
                        <p className="truncate text-xs text-muted-foreground">{row.hint}</p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Features */}
        <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8 lg:py-24">
          <h2 className="max-w-2xl text-3xl font-semibold text-foreground sm:text-4xl">
            Everything a trip needs, nothing it doesn''t.
          </h2>
          <div className="mt-12 grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
            {FEATURES.map((feature) => (
              <article
                key={feature.title}
                className="rounded-3xl border border-border bg-card p-6 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-md"
              >
                <span className="grid size-11 place-items-center rounded-2xl bg-secondary text-primary">
                  <feature.icon className="size-5" aria-hidden />
                </span>
                <h3 className="mt-5 text-base font-semibold text-foreground">{feature.title}</h3>
                <p className="mt-2 text-sm leading-relaxed text-muted-foreground">{feature.body}</p>
              </article>
            ))}
          </div>
        </section>

        {/* How it works */}
        <section className="border-y border-border bg-secondary/40">
          <div className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8 lg:py-24">
            <h2 className="text-3xl font-semibold text-foreground sm:text-4xl">Three steps to a better trip</h2>
            <div className="mt-12 grid gap-8 lg:grid-cols-3">
              {STEPS.map((item) => (
                <div key={item.step} className="min-w-0">
                  <span className="font-display text-4xl font-semibold text-primary/30">{item.step}</span>
                  <h3 className="mt-3 text-lg font-semibold text-foreground">{item.title}</h3>
                  <p className="mt-2 text-sm text-muted-foreground">{item.body}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* AI copilot - NOW LIVE */}
        <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8 lg:py-24">
          <div className="overflow-hidden rounded-[2rem] bg-ink px-6 py-14 text-ink-foreground sm:px-12">
            <div className="grid gap-10 lg:grid-cols-[minmax(0,1.3fr)_minmax(0,1fr)] lg:items-center">
              <div className="min-w-0">
                <span className="inline-flex items-center gap-2 rounded-full bg-accent/20 px-3.5 py-1.5 text-xs font-medium text-accent">
                  <Sparkles className="size-3.5" aria-hidden />
                  Trevvy AI
                </span>
                <h2 className="mt-6 text-3xl font-semibold sm:text-4xl">Meet your AI travel copilot</h2>
                <p className="mt-4 max-w-xl text-sm text-ink-foreground/75 sm:text-base">
                  Plan trips, discover places, build itineraries, and get budget-aware travel recommendations with Trevvy.
                </p>
                <div className="mt-8">
                  <Button 
                    size="lg" 
                    rightIcon={<ArrowRight className="size-4" aria-hidden />}
                    onClick={() => setIsTrevvyOpen(true)}
                  >
                    Chat with Trevvy
                  </Button>
                </div>
              </div>
              <ul className="space-y-3">
                {[
                  "Personalized travel planning",
                  "Budget-aware recommendations",
                  "Nearby places & attractions",
                  "Smart itinerary suggestions",
                ].map((line) => (
                  <li key={line} className="flex items-start gap-3 rounded-2xl bg-ink-foreground/5 px-4 py-3 text-sm">
                    <Star className="mt-0.5 size-4 shrink-0 text-accent" aria-hidden />
                    <span className="text-ink-foreground/85">{line}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </section>

        {/* CTA */}
        <section className="mx-auto max-w-3xl px-4 pb-24 text-center sm:px-6">
          <h2 className="text-3xl font-semibold text-foreground sm:text-4xl">Your next journey starts here</h2>
          <p className="mt-4 text-sm text-muted-foreground sm:text-base">
            Create a free account and turn scattered ideas into one clear plan.
          </p>
          <div className="mt-8 flex justify-center">
            <Link to={primaryTo}>
              <Button size="lg" rightIcon={<ArrowRight className="size-4" aria-hidden />}>
                {primaryLabel}
              </Button>
            </Link>
          </div>
        </section>
      </main>

      <Footer />
      <Trevvy isOpen={isTrevvyOpen} onClose={() => setIsTrevvyOpen(false)} />
    </div>
  );
}
