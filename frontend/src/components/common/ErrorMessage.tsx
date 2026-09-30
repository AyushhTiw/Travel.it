import { AlertTriangle, RefreshCw } from "lucide-react";

import { Button } from "./Button";

export interface ErrorMessageProps {
  title?: string;
  message?: string;
  onRetry?: (() => void) | undefined;
  compact?: boolean;
}

export function ErrorMessage({
  title = "Something went wrong",
  message = "Something went wrong. Please try again.",
  onRetry,
  compact,
}: ErrorMessageProps) {
  if (compact) {
    return (
      <p role="alert" className="flex items-center gap-2 rounded-2xl bg-destructive/10 px-4 py-3 text-sm text-destructive">
        <AlertTriangle className="size-4 shrink-0" aria-hidden />
        {message}
      </p>
    );
  }

  return (
    <div
      role="alert"
      className="flex flex-col items-center justify-center rounded-3xl border border-destructive/25 bg-destructive/5 px-6 py-14 text-center"
    >
      <span className="grid size-14 place-items-center rounded-2xl bg-destructive/12 text-destructive">
        <AlertTriangle className="size-6" aria-hidden />
      </span>
      <h3 className="mt-5 text-lg font-semibold text-foreground">{title}</h3>
      <p className="mt-2 max-w-md text-sm text-muted-foreground">{message}</p>
      {onRetry ? (
        <Button variant="outline" className="mt-6" onClick={onRetry} leftIcon={<RefreshCw className="size-4" aria-hidden />}>
          Try again
        </Button>
      ) : null}
    </div>
  );
}
