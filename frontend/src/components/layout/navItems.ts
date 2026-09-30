import { Compass, LayoutDashboard, Map, MapPin, PiggyBank, Plane } from "lucide-react";
import type { LucideIcon } from "lucide-react";

export interface NavItem {
  to: "/dashboard" | "/explore" | "/destinations" | "/places" | "/trips" | "/budget";
  label: string;
  icon: LucideIcon;
}

export const APP_NAV_ITEMS: readonly NavItem[] = [
  { to: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { to: "/explore", label: "Explore", icon: Compass },
  { to: "/destinations", label: "Destinations", icon: Map },
  { to: "/places", label: "Places", icon: MapPin },
  { to: "/trips", label: "Trips", icon: Plane },
  { to: "/budget", label: "Budget", icon: PiggyBank },
];

export const MOBILE_NAV_ITEMS: readonly NavItem[] = [
  { to: "/dashboard", label: "Home", icon: LayoutDashboard },
  { to: "/explore", label: "Explore", icon: Compass },
  { to: "/trips", label: "Trips", icon: Plane },
  { to: "/budget", label: "Budget", icon: PiggyBank },
];
