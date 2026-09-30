import { useCallback, useEffect, useRef, useState } from "react";

import { Button } from "@/components/common/Button";
import { AppLayout } from "@/components/layout/AppLayout";
import { ExploreSearch } from "@/components/explore/ExploreSearch";
import { LocationButton } from "@/components/explore/LocationButton";
import { NearbyPlaces } from "@/components/explore/NearbyPlaces";
import { TravelCategoryFilter, CATEGORIES } from "@/components/explore/TravelCategoryFilter";
import { TravelMap } from "@/components/map/TravelMap";
import { PlaceDetailsPanel } from "@/components/map/PlaceDetailsPanel";
import { useExplore } from "@/hooks/useExplore";
import { placesService } from "@/services/placesService";
import { toFriendlyMessage } from "@/services/api";
import type { GooglePlace, GooglePlaceDetails } from "@/types/places";

const RADIUS_OPTIONS = [5, 10, 25, 50] as const;
const DEFAULT_COORDS = { lat: 28.6139, lng: 77.209 };

export function ExplorePage() {
  const explore = useExplore();

  // Search parameters - single source of truth
  const [lat, setLat] = useState(DEFAULT_COORDS.lat);
  const [lng, setLng] = useState(DEFAULT_COORDS.lng);
  const [radiusKm, setRadiusKm] = useState(5);
  const [category, setCategory] = useState("all");

  const [query, setQuery] = useState("");
  const [googlePlaces, setGooglePlaces] = useState<GooglePlace[] | null>(null);
  const [isLoadingPlaces, setIsLoadingPlaces] = useState(false);
  const [placesError, setPlacesError] = useState<string | null>(null);

  const [selectedPlace, setSelectedPlace] = useState<GooglePlace | null>(null);
  const [placeDetails, setPlaceDetails] = useState<GooglePlaceDetails | null>(null);
  const [isLoadingDetails, setIsLoadingDetails] = useState(false);
  const [detailsError, setDetailsError] = useState<string | null>(null);

  // Race-condition guard
  const requestIdRef = useRef(0);

  // ─── Fetch whenever search params change ──────────────────────────────────
  useEffect(() => {
    const id = ++requestIdRef.current;
    console.log(`[Explore] fetch #${id} - radius=${radiusKm}km, lat=${lat.toFixed(4)}, lng=${lng.toFixed(4)}, category=${category}`);
    
    setIsLoadingPlaces(true);
    setPlacesError(null);
    
    const fetchPlaces = async () => {
      try {
        const places = await placesService.nearbySearch({
          latitude: lat,
          longitude: lng,
          radius: radiusKm * 1000,   // km → meters
          category: category,
        });
        
        if (id !== requestIdRef.current) {
          console.log(`[Explore] #${id} stale - ignoring`);
          return; // stale
        }
        
        console.log(`[Explore] #${id} got ${places.length} places`);
        setGooglePlaces(places);
      } catch (err) {
        if (id !== requestIdRef.current) return;
        setPlacesError(toFriendlyMessage(err, "Unable to load nearby places."));
        setGooglePlaces([]);
      } finally {
        if (id === requestIdRef.current) setIsLoadingPlaces(false);
      }
    };

    void fetchPlaces();
  }, [lat, lng, radiusKm, category]);

  // ─── Handlers ──────────────────────────────────────────────────────────────
  const handleRadiusChange = (km: number) => {
    console.log(`[Explore] Radius changed to ${km} km`);
    setRadiusKm(km);
  };

  const handleCategoryChange = (cat: string) => {
    console.log(`[Explore] Category changed to ${cat}`);
    setCategory(cat);
  };

  const handleUseLocation = async () => {
    const coords = await explore.requestLocation();
    if (coords) {
      console.log(`[Explore] Using user location: ${coords.latitude}, ${coords.longitude}`);
      setLat(coords.latitude);
      setLng(coords.longitude);
    }
  };

  const handleMarkerClick = useCallback(async (place: GooglePlace) => {
    setSelectedPlace(place);
    setIsLoadingDetails(true);
    setDetailsError(null);
    setPlaceDetails(null);
    try {
      const details = await placesService.getPlaceDetails(place.placeId);
      setPlaceDetails(details);
    } catch (err) {
      setDetailsError(toFriendlyMessage(err, "Unable to load place details."));
    } finally {
      setIsLoadingDetails(false);
    }
  }, []);

  const handleRetry = () => {
    console.log("[Explore] Manual retry triggered");
    requestIdRef.current++; // Force new fetch
    setIsLoadingPlaces(true);
    setPlacesError(null);
  };

  // ─── Derived state ─────────────────────────────────────────────────────────
  const mapCenter = { lat, lng };
  const userLocation = explore.coordinates
    ? { lat: explore.coordinates.latitude, lng: explore.coordinates.longitude }
    : null;

  const categoryLabel = CATEGORIES.find((c) => c.id === category)?.label ?? "Places";

  const visiblePlaces =
    googlePlaces === null
      ? null
      : query.trim() === ""
        ? googlePlaces
        : googlePlaces.filter((p) =>
            [p.name, p.address, p.primaryType].some((f) =>
              (f ?? "").toLowerCase().includes(query.trim().toLowerCase()),
            ),
          );

  const isUsingRealLocation = explore.coordinates !== null;

  // Map zoom based on radius
  const mapZoom = radiusKm <= 5 ? 13 : radiusKm <= 10 ? 12 : radiusKm <= 25 ? 11 : 10;

  return (
    <AppLayout
      title="Explore"
      description="Discover places around you. We only use your location when you ask us to."
    >
      <div className="space-y-6">
        {/* Controls */}
        <section className="rounded-3xl border border-border bg-card p-6">
          <div className="grid gap-5 lg:grid-cols-[minmax(0,1fr)_auto] lg:items-end">
            <ExploreSearch value={query} onChange={setQuery} />
            <LocationButton
              onRequest={() => void handleUseLocation()}
              isLocating={explore.isLocating}
              error={explore.locationError}
            />
          </div>

          <div className="mt-5">
            <p className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
              Search radius
            </p>
            <div className="mt-3 flex flex-wrap gap-2">
              {RADIUS_OPTIONS.map((option) => (
                <Button
                  key={option}
                  size="sm"
                  variant={option === radiusKm ? "primary" : "outline"}
                  aria-pressed={option === radiusKm}
                  onClick={() => handleRadiusChange(option)}
                >
                  {option} km
                </Button>
              ))}
            </div>
          </div>

          <p className="mt-3 text-xs text-muted-foreground">
            {isUsingRealLocation ? (
              <>
                Searching around{" "}
                <span className="font-medium text-foreground">
                  {lat.toFixed(4)}, {lng.toFixed(4)}
                </span>{" "}
                within{" "}
                <span className="font-medium text-foreground">{radiusKm} km</span>
              </>
            ) : (
              <>
                Showing places near{" "}
                <span className="font-medium text-foreground">Delhi, India</span> —
                share your location for local results
              </>
            )}
          </p>
        </section>

        {/* Category filter */}
        <TravelCategoryFilter
          selected={category}
          onChange={handleCategoryChange}
        />

        {/* Map — center and zoom are reactive */}
        <section aria-label="Map">
          <TravelMap
            center={mapCenter}
            zoom={mapZoom}
            places={googlePlaces ?? []}
            selectedPlaceId={selectedPlace?.placeId ?? null}
            onMarkerClick={handleMarkerClick}
            userLocation={userLocation}
            isLoading={isLoadingPlaces}
            error={placesError}
          />
        </section>

        {/* Place Details */}
        {selectedPlace && (
          <PlaceDetailsPanel
            placeDetails={placeDetails}
            isLoading={isLoadingDetails}
            error={detailsError}
            onClose={() => {
              setSelectedPlace(null);
              setPlaceDetails(null);
              setDetailsError(null);
            }}
          />
        )}

        {/* Result count */}
        {googlePlaces !== null && !isLoadingPlaces && (
          <p className="text-sm text-muted-foreground">
            <span className="font-medium text-foreground">
              {visiblePlaces?.length ?? 0}
            </span>{" "}
            {categoryLabel} place{(visiblePlaces?.length ?? 0) !== 1 ? "s" : ""} found
            within{" "}
            <span className="font-medium text-foreground">{radiusKm} km</span>
          </p>
        )}

        {/* Cards */}
        <NearbyPlaces
          places={visiblePlaces}
          isLoading={isLoadingPlaces}
          error={placesError}
          categoryLabel={categoryLabel}
          onRetry={handleRetry}
        />
      </div>
    </AppLayout>
  );
}
