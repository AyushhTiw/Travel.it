import { Search } from "lucide-react";

import { Input } from "@/components/common/Input";

export interface ExploreSearchProps {
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  label?: string;
}

export function ExploreSearch({
  value,
  onChange,
  placeholder = "Search places, cities or countries",
  label = "Search",
}: ExploreSearchProps) {
  return (
    <Input
      type="search"
      label={label}
      placeholder={placeholder}
      icon={<Search className="size-4" aria-hidden />}
      value={value}
      onChange={(event) => onChange(event.target.value)}
    />
  );
}
