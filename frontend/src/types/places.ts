export interface GooglePlace {
  placeId: string;
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  rating: number | null;
  primaryType: string | null;
  googleMapsUrl: string | null;
}

export interface GooglePlaceDetails {
  placeId: string;
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  rating: number | null;
  primaryType: string | null;
  nationalPhoneNumber: string | null;
  internationalPhoneNumber: string | null;
  websiteUri: string | null;
  googleMapsUri: string | null;
  businessStatus: string | null;
  openNow: boolean | null;
  weekdayDescriptions: string[];
}

export interface GooglePlacesTextSearchQuery {
  query: string;
}

export interface GooglePlacesNearbyQuery {
  latitude: number;
  longitude: number;
  radius: number;
  category?: string;
}

export interface GooglePlacesSearchQuery {
  q?: string;
  category?: string;
}
