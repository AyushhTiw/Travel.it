import { useCallback } from "react";

import { categoryService } from "@/services/categoryService";
import { useAsyncData } from "./useAsyncData";
import type { Category } from "@/types/category";

export function useCategories(activeOnly = true) {
  const loader = useCallback(
    () => (activeOnly ? categoryService.getActive() : categoryService.getAll()),
    [activeOnly],
  );
  const state = useAsyncData<Category[]>(loader, [activeOnly], {
    errorMessage: "Unable to load categories.",
  });

  return {
    categories: state.data ?? [],
    isLoading: state.isLoading,
    error: state.error,
    reload: state.reload,
  };
}
