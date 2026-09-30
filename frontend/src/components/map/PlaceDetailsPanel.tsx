import { ExternalLink, MapPin, Phone, Star, X } from "lucide-react";

import { Button } from "@/components/common/Button";
import { Loader } from "@/components/common/Loader";
import type { GooglePlaceDetails } from "@/types/places";

export interface PlaceDetailsPanelProps {
  /** Place details to display */
  placeDetails: GooglePlaceDetails | null;
  /** Loading state */
  isLoading?: boolean;
  /** Error message */
  error?: string | null;
  /** Called when close button is clicked */
  onClose?: () => void;
}

/**
 * Panel displaying detailed information about a selected place
 * Shows name, address, rating, contact info, business status, etc.
 */
export function PlaceDetailsPanel({ placeDetails, isLoading, error, onClose }: PlaceDetailsPanelProps) {
  if (isLoading) {
    return (
      <div className="rounded-3xl border border-border bg-card p-6">
        <Loader label="Loading place details..." />
      </div>
    );
  }

  if (error) {
    return (
      <div className="rounded-3xl border border-border bg-card p-6">
        <div className="flex items-start justify-between">
          <p className="text-sm text-destructive">{error}</p>
          {onClose && (
            <button
              onClick={onClose}
              className="ml-2 rounded-lg p-1 transition-colors hover:bg-secondary"
              aria-label="Close"
            >
              <X className="size-4" />
            </button>
          )}
        </div>
      </div>
    );
  }

  if (!placeDetails) {
    return null;
  }

  const {
    name,
    address,
    rating,
    primaryType,
    businessStatus,
    openNow,
    nationalPhoneNumber,
    internationalPhoneNumber,
    websiteUri,
    googleMapsUri,
    weekdayDescriptions,
  } = placeDetails;

  return (
    <div className="rounded-3xl border border-border bg-card p-6">
      {/* Header with close button */}
      <div className="flex items-start justify-between">
        <div className="min-w-0 flex-1">
          <h3 className="text-lg font-semibold text-foreground">{name}</h3>
          {primaryType && (
            <p className="mt-1 text-sm capitalize text-muted-foreground">
              {primaryType.replace(/_/g, " ")}
            </p>
          )}
        </div>
        {onClose && (
          <button
            onClick={onClose}
            className="ml-2 rounded-lg p-1 transition-colors hover:bg-secondary"
            aria-label="Close details"
          >
            <X className="size-4" />
          </button>
        )}
      </div>

      {/* Rating */}
      {rating && (
        <div className="mt-4 flex items-center gap-1.5">
          <Star className="size-4 fill-yellow-400 text-yellow-400" aria-hidden />
          <span className="text-sm font-medium text-foreground">{rating.toFixed(1)}</span>
        </div>
      )}

      {/* Address */}
      <div className="mt-4 flex items-start gap-2">
        <MapPin className="mt-0.5 size-4 shrink-0 text-primary" aria-hidden />
        <p className="text-sm text-foreground">{address}</p>
      </div>

      {/* Business Status and Open Now */}
      {(businessStatus || openNow !== null) && (
        <div className="mt-4 flex flex-wrap gap-2">
          {businessStatus && (
            <span
              className={`rounded-full px-3 py-1 text-xs font-medium ${
                businessStatus === "OPERATIONAL"
                  ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200"
                  : "bg-gray-100 text-gray-800 dark:bg-gray-800 dark:text-gray-200"
              }`}
            >
              {businessStatus.replace(/_/g, " ")}
            </span>
          )}
          {openNow !== null && (
            <span
              className={`rounded-full px-3 py-1 text-xs font-medium ${
                openNow
                  ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200"
                  : "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200"
              }`}
            >
              {openNow ? "Open now" : "Closed"}
            </span>
          )}
        </div>
      )}

      {/* Contact Info */}
      {(nationalPhoneNumber || internationalPhoneNumber) && (
        <div className="mt-4 flex items-center gap-2">
          <Phone className="size-4 shrink-0 text-primary" aria-hidden />
          <a
            href={`tel:${nationalPhoneNumber || internationalPhoneNumber}`}
            className="text-sm text-primary hover:underline"
          >
            {nationalPhoneNumber || internationalPhoneNumber}
          </a>
        </div>
      )}

      {/* Links */}
      <div className="mt-4 flex flex-wrap gap-2">
        {websiteUri && (
          <Button
            as="a"
            href={websiteUri}
            target="_blank"
            rel="noopener noreferrer"
            size="sm"
            variant="outline"
            leftIcon={<ExternalLink className="size-3.5" aria-hidden />}
          >
            Website
          </Button>
        )}
        {googleMapsUri && (
          <Button
            as="a"
            href={googleMapsUri}
            target="_blank"
            rel="noopener noreferrer"
            size="sm"
            variant="outline"
            leftIcon={<MapPin className="size-3.5" aria-hidden />}
          >
            Open in Google Maps
          </Button>
        )}
      </div>

      {/* Opening Hours */}
      {weekdayDescriptions && weekdayDescriptions.length > 0 && (
        <div className="mt-4">
          <h4 className="text-sm font-semibold text-foreground">Opening Hours</h4>
          <ul className="mt-2 space-y-1">
            {weekdayDescriptions.map((day, index) => (
              <li key={index} className="text-xs text-muted-foreground">
                {day}
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
