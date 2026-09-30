import { X } from "lucide-react";
import { Link } from "@tanstack/react-router";
import { Button } from "@/components/common/Button";

interface LoginRequiredModalProps {
  isOpen: boolean;
  onClose: () => void;
  /** What the user was trying to do, e.g. "save your trip" */
  action?: string;
  /** Called after the user logs in and returns — lets the caller retry the action */
  onLoginSuccess?: () => void;
}

/**
 * Non-blocking modal shown when a guest attempts a protected action
 * (e.g. saving a trip). Does NOT block the entire app on startup.
 */
export function LoginRequiredModal({
  isOpen,
  onClose,
  action = "continue",
  onLoginSuccess,
}: LoginRequiredModalProps) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center p-4">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/50 backdrop-blur-sm animate-in fade-in duration-200"
        onClick={onClose}
        aria-hidden
      />

      {/* Modal */}
      <div className="relative w-full max-w-sm animate-in zoom-in-95 fade-in slide-in-from-bottom-4 duration-300 rounded-3xl border border-border bg-card p-6 shadow-2xl">
        <button
          onClick={onClose}
          className="absolute right-4 top-4 rounded-full p-1.5 text-muted-foreground transition-colors hover:bg-secondary hover:text-foreground"
          aria-label="Close"
        >
          <X className="size-4" />
        </button>

        <div className="mb-1 flex items-center gap-3">
          <div className="flex size-10 items-center justify-center rounded-2xl bg-primary/10">
            <img src="/trevvy-arrow.png" alt="" aria-hidden className="size-6 object-contain" />
          </div>
          <h2 className="font-display text-lg font-semibold text-foreground">Login required</h2>
        </div>

        <p className="mt-3 text-sm text-muted-foreground">
          You need to be logged in to{" "}
          <span className="font-medium text-foreground">{action}</span>.
          <br />
          Your progress has been kept — just log in and continue.
        </p>

        <div className="mt-5 flex flex-col gap-2.5">
          <Link
            to="/login"
            onClick={() => {
              onClose();
              onLoginSuccess?.();
            }}
          >
            <Button className="w-full">Log in</Button>
          </Link>
          <Link to="/signup" onClick={onClose}>
            <Button variant="outline" className="w-full">
              Create account
            </Button>
          </Link>
          <button
            onClick={onClose}
            className="text-sm text-muted-foreground transition-colors hover:text-foreground"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
