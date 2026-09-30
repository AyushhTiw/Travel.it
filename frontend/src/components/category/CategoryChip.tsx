import { cn } from "@/lib/utils";

export interface CategoryChipProps {
  label: string;
  isSelected?: boolean;
  onSelect?: () => void;
}

export function CategoryChip({ label, isSelected = false, onSelect }: CategoryChipProps) {
  return (
    <button
      type="button"
      onClick={onSelect}
      aria-pressed={isSelected}
      className={cn(
        "shrink-0 rounded-full border px-4 py-2 text-sm font-medium transition-colors",
        isSelected
          ? "border-primary bg-primary text-primary-foreground"
          : "border-border bg-card text-muted-foreground hover:border-primary/40 hover:text-foreground",
      )}
    >
      {label}
    </button>
  );
}
