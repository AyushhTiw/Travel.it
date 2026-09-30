import { Skeleton } from "@/components/common/Loader";
import { CategoryChip } from "./CategoryChip";
import type { Category } from "@/types/category";

export interface CategoryFilterProps {
  categories: Category[];
  isLoading: boolean;
  error: string | null;
  selectedId: number | null;
  onSelect: (id: number | null) => void;
}

export function CategoryFilter({ categories, isLoading, error, selectedId, onSelect }: CategoryFilterProps) {
  if (isLoading) {
    return (
      <div className="flex gap-2 overflow-hidden">
        {Array.from({ length: 5 }, (_, index) => (
          <Skeleton key={index} className="h-10 w-24 rounded-full" />
        ))}
      </div>
    );
  }

  if (error) {
    return <p className="text-sm text-muted-foreground">Categories are unavailable right now.</p>;
  }

  if (categories.length === 0) return null;

  return (
    <div role="group" aria-label="Filter by category" className="-mx-1 flex gap-2 overflow-x-auto px-1 pb-1">
      <CategoryChip label="All" isSelected={selectedId === null} onSelect={() => onSelect(null)} />
      {categories.map((category) => (
        <CategoryChip
          key={category.id}
          label={category.name}
          isSelected={selectedId === category.id}
          onSelect={() => onSelect(category.id)}
        />
      ))}
    </div>
  );
}
