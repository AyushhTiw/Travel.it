import { useState } from "react";
import { Plus, Trash2, Wallet } from "lucide-react";

import { Button } from "@/components/common/Button";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { Modal } from "@/components/common/Modal";
import { BudgetItem } from "./BudgetItem";
import { BudgetItemForm } from "./BudgetForm";
import { BudgetSummary } from "./BudgetSummary";
import type { Budget, BudgetItem as BudgetItemType, BudgetItemPayload } from "@/types/budget";

export interface BudgetCardProps {
  budget: Budget;
  isMutating: boolean;
  mutationError: string | null;
  onAddItem: (budgetId: number, payload: BudgetItemPayload) => void;
  onUpdateItem: (budgetId: number, itemId: number, payload: BudgetItemPayload) => void;
  onDeleteItem: (budgetId: number, itemId: number) => void;
  onDeleteBudget: (budgetId: number) => void;
}

export function BudgetCard({
  budget,
  isMutating,
  mutationError,
  onAddItem,
  onUpdateItem,
  onDeleteItem,
  onDeleteBudget,
}: BudgetCardProps) {
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<BudgetItemType | null>(null);
  const [itemToDelete, setItemToDelete] = useState<BudgetItemType | null>(null);
  const [isDeletingBudget, setIsDeletingBudget] = useState(false);
  const items = budget.items ?? [];

  return (
    <section className="space-y-5 rounded-3xl border border-border bg-background p-1">
      <BudgetSummary budget={budget} />

      <div className="px-5 pb-5">
        <div className="grid grid-cols-[minmax(0,1fr)_auto] items-center gap-3">
          <h3 className="truncate text-sm font-semibold uppercase tracking-wide text-muted-foreground">
            Expenses ({items.length})
          </h3>
          <div className="flex shrink-0 gap-2">
            <Button size="sm" onClick={() => setIsAddOpen(true)} leftIcon={<Plus className="size-4" aria-hidden />}>
              Add
            </Button>
            <Button
              size="sm"
              variant="ghost"
              aria-label="Delete budget"
              onClick={() => setIsDeletingBudget(true)}
              leftIcon={<Trash2 className="size-4" aria-hidden />}
            >
              Delete
            </Button>
          </div>
        </div>

        {items.length === 0 ? (
          <p className="mt-4 flex items-center gap-2 rounded-2xl bg-secondary px-4 py-5 text-sm text-muted-foreground">
            <Wallet className="size-4 text-primary" aria-hidden />
            No expenses added to this budget yet.
          </p>
        ) : (
          <ul className="mt-4 space-y-2.5">
            {items.map((item) => (
              <BudgetItem
                key={item.id}
                item={item}
                currency={budget.currency}
                onEdit={setEditingItem}
                onDelete={setItemToDelete}
              />
            ))}
          </ul>
        )}
      </div>

      <Modal isOpen={isAddOpen} onClose={() => setIsAddOpen(false)} title="Add an expense">
        <BudgetItemForm
          isSubmitting={isMutating}
          submitError={mutationError}
          onSubmit={(payload) => {
            onAddItem(budget.id, payload);
            setIsAddOpen(false);
          }}
        />
      </Modal>

      <Modal isOpen={Boolean(editingItem)} onClose={() => setEditingItem(null)} title="Edit expense">
        {editingItem ? (
          <BudgetItemForm
            submitLabel="Save changes"
            isSubmitting={isMutating}
            submitError={mutationError}
            initialValues={{
              category: editingItem.category,
              amount: String(editingItem.amount),
              description: editingItem.description ?? "",
            }}
            onSubmit={(payload) => {
              onUpdateItem(budget.id, editingItem.id, payload);
              setEditingItem(null);
            }}
          />
        ) : null}
      </Modal>

      <ConfirmDialog
        isOpen={Boolean(itemToDelete)}
        title="Delete this expense?"
        description={itemToDelete?.category}
        confirmLabel="Delete"
        isLoading={isMutating}
        onCancel={() => setItemToDelete(null)}
        onConfirm={() => {
          if (itemToDelete) onDeleteItem(budget.id, itemToDelete.id);
          setItemToDelete(null);
        }}
      />

      <ConfirmDialog
        isOpen={isDeletingBudget}
        title="Delete this budget?"
        description="All expenses in it will be removed too."
        confirmLabel="Delete budget"
        isLoading={isMutating}
        onCancel={() => setIsDeletingBudget(false)}
        onConfirm={() => {
          onDeleteBudget(budget.id);
          setIsDeletingBudget(false);
        }}
      />
    </section>
  );
}
