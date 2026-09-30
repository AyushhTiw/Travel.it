import { useState } from "react";
import { PiggyBank, Plus } from "lucide-react";

import { Button } from "@/components/common/Button";
import { EmptyState } from "@/components/common/EmptyState";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { ListSkeleton } from "@/components/common/Loader";
import { Modal } from "@/components/common/Modal";
import { AppLayout } from "@/components/layout/AppLayout";
import { BudgetCard } from "@/components/budget/BudgetCard";
import { BudgetForm } from "@/components/budget/BudgetForm";
import { useBudget } from "@/hooks/useBudget";

export function BudgetPage() {
  const {
    budgets,
    isLoading,
    error,
    reload,
    isMutating,
    mutationError,
    createBudget,
    deleteBudget,
    addItem,
    updateItem,
    deleteItem,
  } = useBudget();
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  return (
    <AppLayout
      title="Budget"
      description="Set what you're willing to spend and watch every expense against it."
      actions={
        <Button onClick={() => setIsCreateOpen(true)} leftIcon={<Plus className="size-4" aria-hidden />}>
          New budget
        </Button>
      }
    >
      {isLoading ? (
        <ListSkeleton rows={3} />
      ) : error ? (
        <ErrorMessage message={error} onRetry={reload} />
      ) : budgets.length === 0 ? (
        <EmptyState
          icon={PiggyBank}
          title="No budgets yet"
          description="Create a budget to track what your trip actually costs."
          action={<Button onClick={() => setIsCreateOpen(true)}>Create a budget</Button>}
        />
      ) : (
        <div className="space-y-8">
          {budgets.map((budget) => (
            <BudgetCard
              key={budget.id}
              budget={budget}
              isMutating={isMutating}
              mutationError={mutationError}
              onAddItem={(budgetId, payload) => void addItem(budgetId, payload)}
              onUpdateItem={(budgetId, itemId, payload) => void updateItem(budgetId, itemId, payload)}
              onDeleteItem={(budgetId, itemId) => void deleteItem(budgetId, itemId)}
              onDeleteBudget={(budgetId) => void deleteBudget(budgetId)}
            />
          ))}
        </div>
      )}

      <Modal isOpen={isCreateOpen} onClose={() => setIsCreateOpen(false)} title="Create a budget">
        <BudgetForm
          isSubmitting={isMutating}
          submitError={mutationError}
          onSubmit={(payload) => {
            void createBudget(payload);
            setIsCreateOpen(false);
          }}
        />
      </Modal>
    </AppLayout>
  );
}
