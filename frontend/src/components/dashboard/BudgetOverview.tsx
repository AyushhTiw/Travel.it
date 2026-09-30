import { Link } from "@tanstack/react-router";
import { PiggyBank } from "lucide-react";

import { Button } from "@/components/common/Button";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Skeleton } from "@/components/common/Loader";
import { BudgetSummary } from "@/components/budget/BudgetSummary";
import type { Budget } from "@/types/budget";

export interface BudgetOverviewProps {
  budgets: Budget[];
  isLoading: boolean;
  error: string | null;
}

export function BudgetOverview({ budgets, isLoading, error }: BudgetOverviewProps) {
  const latest = budgets.at(-1);

  return (
    <section aria-labelledby="budget-overview-heading">
      <div className="mb-4 grid grid-cols-[minmax(0,1fr)_auto] items-center gap-3">
        <h2
          id="budget-overview-heading"
          className="truncate text-sm font-semibold uppercase tracking-wide text-muted-foreground"
        >
          Budget overview
        </h2>
        <Link to="/budget" className="shrink-0 text-sm font-medium text-primary hover:underline">
          Manage
        </Link>
      </div>

      {isLoading ? (
        <Skeleton className="h-44 w-full" />
      ) : error ? (
        <ErrorMessage compact message={error} />
      ) : !latest ? (
        <EmptyState
          icon={PiggyBank}
          title="No budget set"
          description="Set a travel budget and track every expense in one place."
          action={
            <Link to="/budget">
              <Button variant="outline">Create a budget</Button>
            </Link>
          }
        />
      ) : (
        <BudgetSummary budget={latest} />
      )}
    </section>
  );
}
