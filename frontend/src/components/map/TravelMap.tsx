import { useMap, APIProvider, Map, Marker } from "@vis.gl/react-google-maps";
import { useEffect } from "react";
import type { GooglePlace } from "@/types/places";

export interface TravelMapProps {
  center: { lat: number; lng: number };
  zoom?: number;
  places: GooglePlace[];
  selectedPlaceId?: string | null;
  onMarkerClick?: (place: GooglePlace) => void;
  userLocation?: { lat: number; lng: number } | null;
  isLoading?: boolean;
  error?: string | null;
}

/** Inner component that can access the map instance via useMap() */
function MapContent({
  center,
  zoom,
  places,
  selectedPlaceId,
  onMarkerClick,
  userLocation,
}: Pick<TravelMapProps, "center" | "zoom" | "places" | "selectedPlaceId" | "onMarkerClick" | "userLocation">) {
  const map = useMap();

  // Pan map and update zoom whenever center or zoom changes
  useEffect(() => {
    if (map) {
      console.log(`[TravelMap] Updating map: center=(${center.lat.toFixed(4)}, ${center.lng.toFixed(4)}), zoom=${zoom}`);
      map.panTo(center);
      if (zoom !== undefined) {
        map.setZoom(zoom);
      }
    }
  }, [map, center.lat, center.lng, zoom]);

  return (
    <>
      {userLocation && (
        <Marker position={userLocation} title="Your location" />
      )}
      {places.map((place) => (
        <Marker
          key={place.placeId}
          position={{ lat: place.latitude, lng: place.longitude }}
          title={place.name}
          onClick={() => onMarkerClick?.(place)}
        />
      ))}
    </>
  );
}

export function TravelMap({
  center,
  zoom = 12,
  places,
  selectedPlaceId,
  onMarkerClick,
  userLocation,
  isLoading,
  error,
}: TravelMapProps) {
  const apiKey = import.meta.env["VITE_GOOGLE_MAPS_API_KEY"] as string | undefined;

  if (!apiKey) {
    return (
      <div className="flex min-h-[400px] items-center justify-center rounded-3xl border border-destructive bg-destructive/5 p-6">
        <p className="text-sm font-medium text-destructive">
          Google Maps API key is not configured. Add VITE_GOOGLE_MAPS_API_KEY to your .env file.
        </p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex min-h-[400px] items-center justify-center rounded-3xl border border-destructive bg-destructive/5 p-6">
        <p className="text-sm text-destructive">{error}</p>
      </div>
    );
  }

  return (
    <div className="relative overflow-hidden rounded-3xl border border-border bg-muted" style={{ height: "480px" }}>
      {isLoading && (
        <div className="absolute left-1/2 top-4 z-10 -translate-x-1/2 rounded-full bg-card px-4 py-2 text-sm font-medium shadow-lg border border-border">
          Loading places...
        </div>
      )}
      <APIProvider apiKey={apiKey}>
        <Map
          defaultCenter={center}
          defaultZoom={zoom}
          gestureHandling="greedy"
          disableDefaultUI={false}
          style={{ width: "100%", height: "100%" }}
        >
          <MapContent
            center={center}
            zoom={zoom}
            places={places}
            selectedPlaceId={selectedPlaceId}
            onMarkerClick={onMarkerClick}
            userLocation={userLocation}
          />
        </Map>
      </APIProvider>
    </div>
  );
}
