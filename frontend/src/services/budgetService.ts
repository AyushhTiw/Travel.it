import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { Budget, BudgetItem, BudgetItemPayload, BudgetPayload } from "@/types/budget";

export const budgetService = {
  async getAll(): Promise<Budget[]> {
    const { data } = await api.get<Budget[]>(ENDPOINTS.BUDGETS.ROOT);
    return data;
  },

  async getById(id: number | string): Promise<Budget> {
    const { data } = await api.get<Budget>(ENDPOINTS.BUDGETS.BY_ID(id));
    return data;
  },

  async create(payload: BudgetPayload): Promise<Budget> {
    const { data } = await api.post<Budget>(ENDPOINTS.BUDGETS.ROOT, payload);
    return data;
  },

  async remove(id: number | string): Promise<void> {
    await api.delete(ENDPOINTS.BUDGETS.BY_ID(id));
  },

  async addItem(budgetId: number | string, payload: BudgetItemPayload): Promise<BudgetItem> {
    const { data } = await api.post<BudgetItem>(ENDPOINTS.BUDGETS.ITEMS(budgetId), payload);
    return data;
  },

  async updateItem(
    budgetId: number | string,
    itemId: number | string,
    payload: BudgetItemPayload,
  ): Promise<BudgetItem> {
    const { data } = await api.put<BudgetItem>(ENDPOINTS.BUDGETS.ITEM(budgetId, itemId), payload);
    return data;
  },

  async removeItem(budgetId: number | string, itemId: number | string): Promise<void> {
    await api.delete(ENDPOINTS.BUDGETS.ITEM(budgetId, itemId));
  },
};
