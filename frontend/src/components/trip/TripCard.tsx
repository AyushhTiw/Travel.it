import { Link, useNavigate } from "@tanstack/react-router";
import { CalendarDays, Edit2, MapPin, Trash2 } from "lucide-react";
import { useState } from "react";

import { formatDateRange, daysBetween } from "@/utils/formatDate";
import { tripService } from "@/services/tripService";
import { toFriendlyMessage } from "@/services/api";
import type { Trip } from "@/types/trip";

export function TripCard({ trip, onDelete }: { trip: Trip; onDelete?: () => void }) {
  const navigate = useNavigate();
  const [isDeleting, setIsDeleting] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const nights = daysBetween(trip.startDate, trip.endDate);

  const handleEdit = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    navigate({ to: "/trips/$tripId/edit", params: { tripId: String(trip.id) } });
  };

  const handleDeleteClick = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setShowConfirm(true);
  };

  const handleConfirmDelete = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDeleting(true);
    try {
      await tripService.remove(trip.id);
      onDelete?.();
    } catch (err) {
      alert(toFriendlyMessage(err, "Failed to delete trip"));
      setIsDeleting(false);
      setShowConfirm(false);
    }
  };

  const handleCancelDelete = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setShowConfirm(false);
  };

  return (
    <Link
      to="/trips/$tripId"
      params={{ tripId: String(trip.id) }}
      className="group relative block rounded-3xl bg-ink px-6 py-10 text-ink-foreground transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg sm:px-10"
    >
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0 flex-1">
          <p className="text-xs font-semibold uppercase tracking-wide text-accent">Trip</p>
          <h3 className="mt-3 text-3xl font-semibold sm:text-4xl">{trip.title}</h3>
          <div className="mt-5 flex flex-wrap gap-x-6 gap-y-2 text-sm text-ink-foreground/75">
            <span className="flex items-center gap-1.5">
              <MapPin className="size-4 shrink-0" aria-hidden />
              {trip.destination}
            </span>
            <span className="flex items-center gap-1.5">
              <CalendarDays className="size-4 shrink-0" aria-hidden />
              {formatDateRange(trip.startDate, trip.endDate)}
              {nights > 0 ? ` · ${nights} night${nights === 1 ? "" : "s"}` : ""}
            </span>
          </div>
          {trip.description ? (
            <p className="mt-5 max-w-2xl text-sm text-ink-foreground/75">{trip.description}</p>
          ) : null}
        </div>
        <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
          <button
            onClick={handleEdit}
            className="p-2 rounded-lg hover:bg-ink-foreground/10 text-ink-foreground/75 hover:text-accent transition-colors"
            aria-label="Edit trip"
            disabled={isDeleting}
          >
            <Edit2 className="size-5" />
          </button>
          <button
            onClick={handleDeleteClick}
            className="p-2 rounded-lg hover:bg-destructive/20 text-ink-foreground/75 hover:text-destructive transition-colors"
            aria-label="Delete trip"
            disabled={isDeleting}
          >
            <Trash2 className="size-5" />
          </button>
        </div>
      </div>

      {showConfirm && (
        <div
          className="absolute inset-0 bg-ink/98 backdrop-blur-sm rounded-3xl flex flex-col items-center justify-center gap-3 p-6"
          onClick={(e) => e.preventDefault()}
        >
          <p className="text-sm font-medium text-center text-ink-foreground">Delete this trip?</p>
          <div className="flex gap-2">
            <button
              onClick={handleCancelDelete}
              disabled={isDeleting}
              className="px-4 py-2 rounded-lg border border-ink-foreground/20 bg-ink-foreground/10 text-sm font-medium text-ink-foreground hover:bg-ink-foreground/20 transition-colors disabled:opacity-50"
            >
              Cancel
            </button>
            <button
              onClick={handleConfirmDelete}
              disabled={isDeleting}
              className="px-4 py-2 rounded-lg bg-destructive text-destructive-foreground text-sm font-medium hover:bg-destructive/90 transition-colors disabled:opacity-50"
            >
              {isDeleting ? "Deleting..." : "Delete"}
            </button>
          </div>
        </div>
      )}
    </Link>
  );
}
