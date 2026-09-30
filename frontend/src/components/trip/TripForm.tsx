import { useState, type FormEvent } from "react";

import { Button } from "@/components/common/Button";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Input, Textarea } from "@/components/common/Input";
import {
  hasErrors,
  validateTrip,
  type TripFormValues,
  type ValidationErrors,
} from "@/utils/validation";
import type { TripPayload } from "@/types/trip";

const INITIAL: TripFormValues = {
  title: "",
  destination: "",
  startDate: "",
  endDate: "",
  description: "",
};

export interface TripFormProps {
  onSubmit: (payload: TripPayload) => void;
  isSubmitting?: boolean;
  submitError?: string | null;
  submitLabel?: string;
  initialValues?: Partial<TripFormValues>;
}

export function TripForm({
  onSubmit,
  isSubmitting = false,
  submitError,
  submitLabel = "Save trip",
  initialValues,
}: TripFormProps) {
  const [values, setValues] = useState<TripFormValues>({ ...INITIAL, ...initialValues });
  const [errors, setErrors] = useState<ValidationErrors<TripFormValues>>({});

  const update = (field: keyof TripFormValues, value: string) =>
    setValues((current) => ({ ...current, [field]: value }));

  const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const validation = validateTrip(values);
    setErrors(validation);
    if (hasErrors(validation)) return;
    onSubmit({
      title: values.title.trim(),
      destination: values.destination.trim(),
      startDate: values.startDate,
      endDate: values.endDate,
      description: values.description.trim(),
    });
  };

  return (
    <form onSubmit={handleSubmit} noValidate className="space-y-5">
      {submitError ? <ErrorMessage compact message={submitError} /> : null}
      <Input
        label="Trip title"
        placeholder="Weekend in the mountains"
        value={values.title}
        onChange={(event) => update("title", event.target.value)}
        error={errors.title}
        required
      />
      <Input
        label="Destination"
        placeholder="Where are you heading?"
        value={values.destination}
        onChange={(event) => update("destination", event.target.value)}
        error={errors.destination}
        required
      />
      <div className="grid gap-5 sm:grid-cols-2">
        <Input
          label="Start date"
          type="date"
          value={values.startDate}
          onChange={(event) => update("startDate", event.target.value)}
          error={errors.startDate}
          required
        />
        <Input
          label="End date"
          type="date"
          value={values.endDate}
          onChange={(event) => update("endDate", event.target.value)}
          error={errors.endDate}
          required
        />
      </div>
      <Textarea
        label="Notes"
        placeholder="What do you want this trip to feel like?"
        value={values.description}
        onChange={(event) => update("description", event.target.value)}
        error={errors.description}
      />
      <Button type="submit" size="lg" isLoading={isSubmitting}>
        {submitLabel}
      </Button>
    </form>
  );
}
