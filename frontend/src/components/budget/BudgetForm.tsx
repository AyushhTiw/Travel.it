import { useState, type FormEvent } from "react";

import { Button } from "@/components/common/Button";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Input } from "@/components/common/Input";
import {
  hasErrors,
  validateBudget,
  validateBudgetItem,
  type BudgetFormValues,
  type BudgetItemFormValues,
  type ValidationErrors,
} from "@/utils/validation";
import type { BudgetItemPayload, BudgetPayload } from "@/types/budget";

export interface BudgetFormProps {
  onSubmit: (payload: BudgetPayload) => void;
  isSubmitting?: boolean;
  submitError?: string | null;
}

export function BudgetForm({ onSubmit, isSubmitting, submitError }: BudgetFormProps) {
  const [values, setValues] = useState<BudgetFormValues>({ totalAmount: "", currency: "INR" });
  const [errors, setErrors] = useState<ValidationErrors<BudgetFormValues>>({});

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const validation = validateBudget(values);
    setErrors(validation);
    if (hasErrors(validation)) return;
    onSubmit({ totalAmount: Number(values.totalAmount), currency: values.currency.trim().toUpperCase() });
    setValues({ totalAmount: "", currency: values.currency });
  };

  return (
    <form onSubmit={handleSubmit} noValidate className="space-y-5">
      {submitError ? <ErrorMessage compact message={submitError} /> : null}
      <div className="grid gap-5 sm:grid-cols-2">
        <Input
          label="Total amount"
          type="number"
          min={1}
          step="1"
          placeholder="50000"
          value={values.totalAmount}
          onChange={(event) => setValues((current) => ({ ...current, totalAmount: event.target.value }))}
          error={errors.totalAmount}
          required
        />
        <Input
          label="Currency"
          placeholder="INR"
          maxLength={3}
          value={values.currency}
          onChange={(event) => setValues((current) => ({ ...current, currency: event.target.value.toUpperCase() }))}
          error={errors.currency}
          required
        />
      </div>
      <Button type="submit" isLoading={isSubmitting}>
        Create budget
      </Button>
    </form>
  );
}

export interface BudgetItemFormProps {
  onSubmit: (payload: BudgetItemPayload) => void;
  isSubmitting?: boolean;
  submitError?: string | null;
  initialValues?: Partial<BudgetItemFormValues>;
  submitLabel?: string;
}

export function BudgetItemForm({
  onSubmit,
  isSubmitting,
  submitError,
  initialValues,
  submitLabel = "Add expense",
}: BudgetItemFormProps) {
  const [values, setValues] = useState<BudgetItemFormValues>({
    category: "",
    amount: "",
    description: "",
    ...initialValues,
  });
  const [errors, setErrors] = useState<ValidationErrors<BudgetItemFormValues>>({});

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const validation = validateBudgetItem(values);
    setErrors(validation);
    if (hasErrors(validation)) return;
    onSubmit({
      category: values.category.trim(),
      amount: Number(values.amount),
      description: values.description.trim(),
    });
  };

  return (
    <form onSubmit={handleSubmit} noValidate className="space-y-5">
      {submitError ? <ErrorMessage compact message={submitError} /> : null}
      <div className="grid gap-5 sm:grid-cols-2">
        <Input
          label="Category"
          placeholder="Stay, food, transport…"
          value={values.category}
          onChange={(event) => setValues((current) => ({ ...current, category: event.target.value }))}
          error={errors.category}
          required
        />
        <Input
          label="Amount"
          type="number"
          min={1}
          step="1"
          placeholder="4500"
          value={values.amount}
          onChange={(event) => setValues((current) => ({ ...current, amount: event.target.value }))}
          error={errors.amount}
          required
        />
      </div>
      <Input
        label="Note"
        placeholder="Optional detail"
        value={values.description}
        onChange={(event) => setValues((current) => ({ ...current, description: event.target.value }))}
      />
      <Button type="submit" isLoading={isSubmitting}>
        {submitLabel}
      </Button>
    </form>
  );
}
