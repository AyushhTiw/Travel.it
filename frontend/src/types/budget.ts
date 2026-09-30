export interface BudgetItem {
  id: number;
  category: string;
  amount: number;
  description: string;
}

export interface Budget {
  id: number;
  totalAmount: number;
  currency: string;
  items: BudgetItem[];
}

export interface BudgetPayload {
  totalAmount: number;
  currency: string;
}

export interface BudgetItemPayload {
  category: string;
  amount: number;
  description: string;
}
