import { Link } from "@tanstack/react-router";

import { Logo } from "@/components/common/Logo";

const FOOTER_LINKS = [
  { to: "/explore", label: "Explore" },
  { to: "/destinations", label: "Destinations" },
  { to: "/trips", label: "Trips" },
  { to: "/budget", label: "Budget" },
] as const;

export function Footer() {
  return (
    <footer className="border-t border-border bg-ink text-ink-foreground">
      <div className="mx-auto max-w-7xl px-4 py-14 sm:px-6 lg:px-8">
        <div className="grid gap-10 md:grid-cols-[minmax(0,1.5fr)_minmax(0,1fr)]">
          <div className="min-w-0">
            <Logo invert showTagline />
            <p className="mt-4 max-w-md text-sm text-ink-foreground/70">
              Plan less. Experience more. Travel.it helps you discover places, plan trips and travel smarter.
            </p>
          </div>
          <div>
            <h2 className="text-sm font-semibold text-ink-foreground">Product</h2>
            <ul className="mt-4 grid grid-cols-2 gap-2 text-sm">
              {FOOTER_LINKS.map((link) => (
                <li key={link.to}>
                  <Link to={link.to} className="text-ink-foreground/70 transition-colors hover:text-ink-foreground">
                    {link.label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        </div>
        <p className="mt-12 border-t border-ink-foreground/10 pt-6 text-xs text-ink-foreground/50">
          © {new Date().getFullYear()} Travel.it — Your AI Travel Buddy.
        </p>
      </div>
    </footer>
  );
}
