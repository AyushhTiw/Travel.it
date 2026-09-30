import type { ReactNode } from "react";

import { MobileNavigation } from "./MobileNavigation";
import { Navbar } from "./Navbar";
import { Sidebar } from "./Sidebar";

export interface AppLayoutProps {
  children: ReactNode;
  title?: string;
  description?: string;
  actions?: ReactNode;
  withSidebar?: boolean;
}

export function AppLayout({ children, title, description, actions, withSidebar = true }: AppLayoutProps) {
  return (
    <div className="flex min-h-screen flex-col bg-background">
      <Navbar />
      <div className="mx-auto flex w-full max-w-7xl flex-1 gap-8 px-4 pb-28 pt-8 sm:px-6 lg:px-8 lg:pb-16">
        {withSidebar ? <Sidebar /> : null}
        <main className="min-w-0 flex-1">
          {title ? (
            <div className="mb-8 grid grid-cols-[minmax(0,1fr)_auto] items-start gap-4 sm:flex sm:flex-wrap sm:items-center sm:justify-between">
              <div className="min-w-0">
                <h1 className="text-2xl font-semibold text-foreground sm:text-3xl">{title}</h1>
                {description ? <p className="mt-2 text-sm text-muted-foreground">{description}</p> : null}
              </div>
              {actions ? <div className="shrink-0">{actions}</div> : null}
            </div>
          ) : null}
          {children}
        </main>
      </div>
      <MobileNavigation />
    </div>
  );
}
