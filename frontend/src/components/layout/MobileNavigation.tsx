import { Link } from "@tanstack/react-router";

import { MOBILE_NAV_ITEMS } from "./navItems";

export function MobileNavigation() {
  return (
    <nav
      aria-label="Primary mobile"
      className="fixed inset-x-0 bottom-0 z-40 border-t border-border glass-panel lg:hidden"
    >
      <ul className="mx-auto grid max-w-lg grid-cols-4">
        {MOBILE_NAV_ITEMS.map((item) => (
          <li key={item.to}>
            <Link
              to={item.to}
              activeProps={{ className: "text-primary" }}
              className="flex flex-col items-center gap-1 px-2 py-3 text-[11px] font-medium text-muted-foreground transition-colors"
            >
              <item.icon className="size-5" aria-hidden />
              {item.label}
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
}
