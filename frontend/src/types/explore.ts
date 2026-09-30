export interface NearbyPlace {
  id: number;
  name: string;
  description: string;
  city: string;
  state: string;
  country: string;
  latitude: number;
  longitude: number;
  address: string;
  distanceKm: number;
}

export interface NearbyQuery {
  latitude: number;
  longitude: number;
  radiusKm: number;
}

export interface Coordinates {
  latitude: number;
  longitude: number;
}
