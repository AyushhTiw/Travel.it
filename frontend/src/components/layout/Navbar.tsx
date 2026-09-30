import { useState } from "react";
import { Link, useNavigate } from "@tanstack/react-router";
import { LogOut, Menu, User as UserIcon, X } from "lucide-react";

import { Button } from "@/components/common/Button";
import { Logo } from "@/components/common/Logo";
import { useAuth } from "@/hooks/useAuth";
import { cn } from "@/lib/utils";
import { APP_NAV_ITEMS } from "./navItems";

export function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = async () => {
    setIsMenuOpen(false);
    await logout();
    void navigate({ to: "/login", replace: true });
  };

  return (
    <header className="sticky top-0 z-40 w-full border-b border-border/70 glass-panel">
      <nav
        aria-label="Main"
        className="mx-auto grid max-w-7xl grid-cols-[minmax(0,1fr)_auto] items-center gap-4 px-4 py-3 sm:px-6 lg:px-8"
      >
        <div className="flex min-w-0 items-center gap-8">
          <Link to={isAuthenticated ? "/dashboard" : "/"} aria-label="Travel.it home">
            <Logo showTagline />
          </Link>
          {isAuthenticated ? (
            <ul className="hidden items-center gap-1 lg:flex">
              {APP_NAV_ITEMS.map((item) => (
                <li key={item.to}>
                  <Link
                    to={item.to}
                    activeProps={{ className: "bg-secondary text-foreground" }}
                    className="rounded-full px-4 py-2 text-sm font-medium text-muted-foreground transition-colors hover:bg-secondary hover:text-foreground"
                  >
                    {item.label}
                  </Link>
                </li>
              ))}
            </ul>
          ) : null}
        </div>

        <div className="flex shrink-0 items-center gap-2">
          {isAuthenticated && user ? (
            <>
              <Link
                to="/profile"
                className="hidden items-center gap-2.5 rounded-full border border-border bg-card px-2 py-1.5 pr-4 text-sm transition-colors hover:bg-secondary sm:inline-flex"
              >
                <span className="grid size-7 place-items-center rounded-full bg-primary text-xs font-semibold text-primary-foreground">
                  {user.name.charAt(0).toUpperCase()}
                </span>
                <span className="max-w-32 truncate font-medium text-foreground">{user.name}</span>
              </Link>
              <Button
                variant="ghost"
                size="sm"
                className="hidden sm:inline-flex"
                onClick={handleLogout}
                leftIcon={<LogOut className="size-4" aria-hidden />}
              >
                Logout
              </Button>
            </>
          ) : (
            <div className="hidden items-center gap-2 sm:flex">
              <Link to="/login">
                <Button variant="ghost" size="sm">
                  Log in
                </Button>
              </Link>
              <Link to="/signup">
                <Button size="sm">Get started</Button>
              </Link>
            </div>
          )}

          <button
            type="button"
            aria-label={isMenuOpen ? "Close menu" : "Open menu"}
            aria-expanded={isMenuOpen}
            onClick={() => setIsMenuOpen((open) => !open)}
            className="rounded-full p-2.5 text-foreground transition-colors hover:bg-secondary lg:hidden"
          >
            {isMenuOpen ? <Menu className="size-5" aria-hidden /> : <Menu className="size-5" aria-hidden />}
          </button>
        </div>
      </nav>

      {isMenuOpen ? (
        <div className="border-t border-border bg-card px-4 py-4 lg:hidden">
          <div className="mb-3 flex items-center justify-between">
            <span className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">Menu</span>
            <button
              type="button"
              aria-label="Close menu"
              onClick={() => setIsMenuOpen(false)}
              className="rounded-full p-1.5 text-muted-foreground hover:bg-secondary"
            >
              <X className="size-4" aria-hidden />
            </button>
          </div>
          <ul className="space-y-1">
            {isAuthenticated
              ? APP_NAV_ITEMS.map((item) => (
                  <li key={item.to}>
                    <Link
                      to={item.to}
                      onClick={() => setIsMenuOpen(false)}
                      activeProps={{ className: "bg-secondary" }}
                      className={cn(
                        "flex items-center gap-3 rounded-2xl px-4 py-3 text-sm font-medium text-foreground transition-colors hover:bg-secondary",
                      )}
                    >
                      <item.icon className="size-4 text-primary" aria-hidden />
                      {item.label}
                    </Link>
                  </li>
                ))
              : null}
          </ul>
          <div className="mt-4 flex flex-col gap-2">
            {isAuthenticated ? (
              <>
                <Link to="/profile" onClick={() => setIsMenuOpen(false)}>
                  <Button variant="outline" className="w-full" leftIcon={<UserIcon className="size-4" aria-hidden />}>
                    Profile
                  </Button>
                </Link>
                <Button
                  variant="ghost"
                  className="w-full"
                  onClick={handleLogout}
                  leftIcon={<LogOut className="size-4" aria-hidden />}
                >
                  Logout
                </Button>
              </>
            ) : (
              <>
                <Link to="/login" onClick={() => setIsMenuOpen(false)}>
                  <Button variant="outline" className="w-full">
                    Log in
                  </Button>
                </Link>
                <Link to="/signup" onClick={() => setIsMenuOpen(false)}>
                  <Button className="w-full">Get started</Button>
                </Link>
              </>
            )}
          </div>
        </div>
      ) : null}
    </header>
  );
}
