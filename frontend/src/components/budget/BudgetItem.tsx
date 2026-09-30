import { Pencil, Trash2 } from "lucide-react";

import { formatCurrency } from "@/utils/formatCurrency";
import type { BudgetItem as BudgetItemType } from "@/types/budget";

export interface BudgetItemProps {
  item: BudgetItemType;
  currency: string;
  onEdit: (item: BudgetItemType) => void;
  onDelete: (item: BudgetItemType) => void;
}

export function BudgetItem({ item, currency, onEdit, onDelete }: BudgetItemProps) {
  return (
    <li className="grid grid-cols-[minmax(0,1fr)_auto] items-center gap-4 rounded-2xl border border-border bg-card px-5 py-4">
      <div className="min-w-0">
        <p className="truncate text-sm font-semibold text-foreground">{item.category}</p>
        {item.description ? (
          <p className="mt-0.5 truncate text-xs text-muted-foreground">{item.description}</p>
        ) : null}
      </div>
      <div className="flex shrink-0 items-center gap-1.5">
        <span className="mr-1 text-sm font-semibold text-foreground">{formatCurrency(item.amount, currency)}</span>
        <button
          type="button"
          onClick={() => onEdit(item)}
          aria-label={`Edit ${item.category}`}
          className="rounded-full p-2 text-muted-foreground transition-colors hover:bg-secondary hover:text-foreground"
        >
          <Pencil className="size-4" aria-hidden />
        </button>
        <button
          type="button"
          onClick={() => onDelete(item)}
          aria-label={`Delete ${item.category}`}
          className="rounded-full p-2 text-muted-foreground transition-colors hover:bg-destructive/10 hover:text-destructive"
        >
          <Trash2 className="size-4" aria-hidden />
        </button>
      </div>
    </li>
  );
}
