import { formatCurrency, percentage } from "@/utils/formatCurrency";
import type { Budget } from "@/types/budget";

export function computeBudgetTotals(budget: Budget) {
  const allocated = (budget.items ?? []).reduce((sum, item) => sum + (item.amount ?? 0), 0);
  const remaining = (budget.totalAmount ?? 0) - allocated;
  return { allocated, remaining, used: percentage(allocated, budget.totalAmount ?? 0) };
}

export function BudgetSummary({ budget }: { budget: Budget }) {
  const { allocated, remaining, used } = computeBudgetTotals(budget);

  const stats = [
    { label: "Total budget", value: formatCurrency(budget.totalAmount, budget.currency) },
    { label: "Allocated", value: formatCurrency(allocated, budget.currency) },
    {
      label: "Remaining",
      value: formatCurrency(remaining, budget.currency),
      tone: remaining < 0 ? "text-destructive" : "text-success",
    },
  ];

  return (
    <div className="rounded-3xl border border-border bg-card p-6">
      <div className="grid gap-5 sm:grid-cols-3">
        {stats.map((stat) => (
          <div key={stat.label} className="min-w-0">
            <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">{stat.label}</p>
            <p className={`mt-1.5 truncate text-xl font-semibold ${stat.tone ?? "text-foreground"}`}>{stat.value}</p>
          </div>
        ))}
      </div>
      <div className="mt-6">
        <div
          role="progressbar"
          aria-valuenow={used}
          aria-valuemin={0}
          aria-valuemax={100}
          aria-label="Budget used"
          className="h-2.5 w-full overflow-hidden rounded-full bg-secondary"
        >
          <div
            className={`h-full rounded-full transition-all duration-500 ${used >= 100 ? "bg-destructive" : "bg-primary"}`}
            style={{ width: `${used}%` }}
          />
        </div>
        <p className="mt-2 text-xs text-muted-foreground">{used}% of this budget is allocated.</p>
      </div>
    </div>
  );
}
