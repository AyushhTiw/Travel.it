import type { LucideIcon } from "lucide-react";
import { Compass } from "lucide-react";
import type { ReactNode } from "react";

export interface EmptyStateProps {
  icon?: LucideIcon;
  title: string;
  description?: string;
  action?: ReactNode;
}

export function EmptyState({ icon: Icon = Compass, title, description, action }: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center rounded-3xl border border-dashed border-border bg-card/60 px-6 py-16 text-center">
      <span className="grid size-14 place-items-center rounded-2xl bg-secondary text-primary">
        <Icon className="size-6" aria-hidden />
      </span>
      <h3 className="mt-5 text-lg font-semibold text-foreground">{title}</h3>
      {description ? <p className="mt-2 max-w-md text-sm text-muted-foreground">{description}</p> : null}
      {action ? <div className="mt-6">{action}</div> : null}
    </div>
  );
}
