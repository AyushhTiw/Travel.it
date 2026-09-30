import { useCallback, useState } from "react";

import { budgetService } from "@/services/budgetService";
import { toFriendlyMessage } from "@/services/api";
import { useAsyncData } from "./useAsyncData";
import type { Budget, BudgetItemPayload, BudgetPayload } from "@/types/budget";

export function useBudget() {
  const loader = useCallback(() => budgetService.getAll(), []);
  const state = useAsyncData<Budget[]>(loader, [], {
    errorMessage: "Unable to load your budgets.",
  });

  const [isMutating, setIsMutating] = useState(false);
  const [mutationError, setMutationError] = useState<string | null>(null);

  const run = useCallback(
    async <T,>(action: () => Promise<T>, fallback: string): Promise<T | null> => {
      setIsMutating(true);
      setMutationError(null);
      try {
        const result = await action();
        state.reload();
        return result;
      } catch (err: unknown) {
        setMutationError(toFriendlyMessage(err, fallback));
        return null;
      } finally {
        setIsMutating(false);
      }
    },
    [state],
  );

  const createBudget = useCallback(
    (payload: BudgetPayload) => run(() => budgetService.create(payload), "Unable to create this budget."),
    [run],
  );

  const deleteBudget = useCallback(
    (id: number) => run(() => budgetService.remove(id), "Unable to delete this budget."),
    [run],
  );

  const addItem = useCallback(
    (budgetId: number, payload: BudgetItemPayload) =>
      run(() => budgetService.addItem(budgetId, payload), "Unable to add this expense."),
    [run],
  );

  const updateItem = useCallback(
    (budgetId: number, itemId: number, payload: BudgetItemPayload) =>
      run(() => budgetService.updateItem(budgetId, itemId, payload), "Unable to update this expense."),
    [run],
  );

  const deleteItem = useCallback(
    (budgetId: number, itemId: number) =>
      run(() => budgetService.removeItem(budgetId, itemId), "Unable to delete this expense."),
    [run],
  );

  return {
    budgets: state.data ?? [],
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
    isMutating,
    mutationError,
    createBudget,
    deleteBudget,
    addItem,
    updateItem,
    deleteItem,
  };
}
