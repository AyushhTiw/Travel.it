interface Category {
  id: string;
  label: string;
  emoji: string;
}

const CATEGORIES: Category[] = [
  { id: "all",           label: "All",          emoji: "🌐" },
  { id: "tourist_spots", label: "Tourist Spots", emoji: "🏛️" },
  { id: "best_spots",    label: "Best Spots",    emoji: "⭐" },
  { id: "adventure",     label: "Adventure",     emoji: "🧗" },
  { id: "sports",        label: "Sports",        emoji: "🏟️" },
  { id: "food",          label: "Food",          emoji: "🍽️" },
  { id: "cafes",         label: "Cafes",         emoji: "☕" },
  { id: "hotels",        label: "Hotels",        emoji: "🏨" },
  { id: "shopping",      label: "Shopping",      emoji: "🛍️" },
  { id: "luxury",        label: "Luxury",        emoji: "💎" },
  { id: "nature",        label: "Nature",        emoji: "🌿" },
  { id: "entertainment", label: "Entertainment", emoji: "🎭" },
  { id: "family",        label: "Family",        emoji: "👨‍👩‍👧" },
];

interface TravelCategoryFilterProps {
  selected: string;
  onChange: (category: string) => void;
}

export function TravelCategoryFilter({ selected, onChange }: TravelCategoryFilterProps) {
  return (
    <div className="overflow-x-auto pb-1 -mb-1">
      <div className="flex gap-2 min-w-max">
        {CATEGORIES.map((cat) => {
          const isActive = selected === cat.id;
          return (
            <button
              key={cat.id}
              onClick={() => onChange(cat.id)}
              className={[
                "flex items-center gap-1.5 rounded-full px-4 py-2 text-sm font-medium whitespace-nowrap",
                "border transition-all duration-200 hover:scale-[1.03] active:scale-[0.97]",
                isActive
                  ? "bg-primary text-primary-foreground border-primary shadow-sm"
                  : "bg-card text-foreground border-border hover:border-primary/50 hover:bg-secondary",
              ].join(" ")}
            >
              <span aria-hidden>{cat.emoji}</span>
              {cat.label}
            </button>
          );
        })}
      </div>
    </div>
  );
}

export { CATEGORIES };
export type { Category };
