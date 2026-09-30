import { LocateFixed } from "lucide-react";

import { Button } from "@/components/common/Button";

export interface LocationButtonProps {
  onRequest: () => void;
  isLocating: boolean;
  error?: string | null;
}

/** Geolocation is only ever requested from this explicit user action. */
export function LocationButton({ onRequest, isLocating, error }: LocationButtonProps) {
  return (
    <div>
      <Button
        type="button"
        variant="outline"
        onClick={onRequest}
        isLoading={isLocating}
        leftIcon={<LocateFixed className="size-4" aria-hidden />}
      >
        Use my location
      </Button>
      {error ? (
        <p role="alert" className="mt-2 text-xs text-destructive">
          {error}
        </p>
      ) : null}
    </div>
  );
}
