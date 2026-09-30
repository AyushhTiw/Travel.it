import { cn } from "@/lib/utils";

export interface LogoProps {
  className?: string;
  showTagline?: boolean;
  invert?: boolean;
}

export function Logo({ className, showTagline = false, invert = false }: LogoProps) {
  return (
    <span className={cn("inline-flex items-center gap-2.5", className)}>
      <img
        src="/travel-it-logo.png"
        alt=""
        aria-hidden
        className={cn("size-9 object-contain", invert && "brightness-0 invert")}
      />
      <span className="flex min-w-0 flex-col leading-none">
        <span
          className={cn(
            "font-wordmark text-xl font-light tracking-wide",
            invert ? "text-ink-foreground" : "text-foreground",
          )}
        >
          Travel.it
        </span>
        {showTagline ? (
          <span className={cn("mt-1 text-[11px] font-medium", invert ? "text-ink-foreground/70" : "text-muted-foreground")}>
            Your AI Travel Buddy
          </span>
        ) : null}
      </span>
    </span>
  );
}
