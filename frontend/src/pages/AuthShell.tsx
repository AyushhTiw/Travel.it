import { Link } from "@tanstack/react-router";
import { ArrowLeft } from "lucide-react";
import type { ReactNode } from "react";

import { Logo } from "@/components/common/Logo";

export interface AuthShellProps {
  title: string;
  subtitle: string;
  children: ReactNode;
  footer?: ReactNode;
}

export function AuthShell({ title, subtitle, children, footer }: AuthShellProps) {
  return (
    <div className="grid min-h-screen bg-background lg:grid-cols-2">
      <div className="flex min-w-0 flex-col px-4 py-8 sm:px-8 lg:px-16">
        <div className="grid grid-cols-[minmax(0,1fr)_auto] items-center gap-4">
          <Link to="/" aria-label="Travel.it home" className="min-w-0">
            <Logo showTagline />
          </Link>
          <Link
            to="/"
            className="inline-flex shrink-0 items-center gap-1.5 text-sm text-muted-foreground transition-colors hover:text-foreground"
          >
            <ArrowLeft className="size-4" aria-hidden />
            Home
          </Link>
        </div>

        <div className="mx-auto flex w-full max-w-md flex-1 flex-col justify-center py-12">
          <h1 className="text-3xl font-semibold text-foreground">{title}</h1>
          <p className="mt-2.5 text-sm text-muted-foreground">{subtitle}</p>
          <div className="mt-9">{children}</div>
          {footer ? <div className="mt-8">{footer}</div> : null}
        </div>
      </div>

      <aside className="relative hidden flex-col justify-between bg-ink p-12 text-ink-foreground lg:flex">
        <Logo invert showTagline />
        <div>
          <img src="/travel-it-logo.png" alt="" aria-hidden className="w-40 object-contain brightness-0 invert" />
          <h2 className="mt-8 max-w-sm text-3xl font-semibold leading-tight">Plan less. Experience more.</h2>
          <p className="mt-4 max-w-sm text-sm text-ink-foreground/70">
            Your intelligent travel companion for discovering places, planning trips, and traveling smarter.
          </p>
        </div>
        <p className="text-xs text-ink-foreground/50">© {new Date().getFullYear()} Travel.it</p>
      </aside>
    </div>
  );
}
