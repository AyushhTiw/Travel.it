export function formatDate(value: string | Date | undefined | null): string {
  if (!value) return "—";
  const date = typeof value === "string" ? new Date(value) : value;
  if (Number.isNaN(date.getTime())) return "—";
  return new Intl.DateTimeFormat(undefined, {
    day: "numeric",
    month: "short",
    year: "numeric",
  }).format(date);
}

export function formatDateRange(start?: string | null, end?: string | null): string {
  if (!start && !end) return "Dates not set";
  return `${formatDate(start)} — ${formatDate(end)}`;
}

export function daysBetween(start: string, end: string): number {
  const startDate = new Date(start).getTime();
  const endDate = new Date(end).getTime();
  if (Number.isNaN(startDate) || Number.isNaN(endDate)) return 0;
  return Math.max(0, Math.round((endDate - startDate) / 86_400_000));
}

export function isUpcoming(startDate: string): boolean {
  const date = new Date(startDate).getTime();
  if (Number.isNaN(date)) return false;
  return date >= Date.now() - 86_400_000;
}
