import { Link, useNavigate } from "@tanstack/react-router";
import { CalendarDays, Edit2, MapPin, Trash2 } from "lucide-react";
import { useState } from "react";

import { formatDateRange } from "@/utils/formatDate";
import { tripService } from "@/services/tripService";
import { toFriendlyMessage } from "@/services/api";
import type { Trip } from "@/types/trip";

export function TripCard({ trip, onDelete }: { trip: Trip; onDelete?: () => void }) {
  const navigate = useNavigate();
  const [isDeleting, setIsDeleting] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

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
      className="group flex flex-col rounded-3xl border border-border bg-card p-6 transition-all duration-200 hover:-translate-y-0.5 hover:shadow-lg relative"
    >
      <div className="flex items-start justify-between gap-2">
        <h3 className="truncate text-lg font-semibold text-foreground">{trip.title}</h3>
        <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
          <button
            onClick={handleEdit}
            className="p-1.5 rounded-lg hover:bg-secondary text-muted-foreground hover:text-primary transition-colors"
            aria-label="Edit trip"
            disabled={isDeleting}
          >
            <Edit2 className="size-4" />
          </button>
          <button
            onClick={handleDeleteClick}
            className="p-1.5 rounded-lg hover:bg-destructive/10 text-muted-foreground hover:text-destructive transition-colors"
            aria-label="Delete trip"
            disabled={isDeleting}
          >
            <Trash2 className="size-4" />
          </button>
        </div>
      </div>
      
      <p className="mt-1.5 flex min-w-0 items-center gap-1.5 text-sm text-muted-foreground">
        <MapPin className="size-3.5 shrink-0 text-primary" aria-hidden />
        <span className="truncate">{trip.destination}</span>
      </p>
      <p className="mt-3 flex items-center gap-1.5 text-sm text-muted-foreground">
        <CalendarDays className="size-3.5 shrink-0 text-primary" aria-hidden />
        {formatDateRange(trip.startDate, trip.endDate)}
      </p>
      {trip.description ? (
        <p className="mt-4 line-clamp-2 text-sm text-muted-foreground">{trip.description}</p>
      ) : null}

      {showConfirm && (
        <div
          className="absolute inset-0 bg-card/95 backdrop-blur-sm rounded-3xl flex flex-col items-center justify-center gap-3 p-6"
          onClick={(e) => e.preventDefault()}
        >
          <p className="text-sm font-medium text-center">Delete this trip?</p>
          <div className="flex gap-2">
            <button
              onClick={handleCancelDelete}
              disabled={isDeleting}
              className="px-4 py-2 rounded-lg border border-border bg-background text-sm font-medium hover:bg-secondary transition-colors disabled:opacity-50"
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