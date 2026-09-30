export interface Place {
  id: number;
  name: string;
  description: string;
  city: string;
  state: string;
  country: string;
  latitude: number;
  longitude: number;
  address: string;
  active: boolean;
}

export interface PlacePayload {
  name: string;
  description: string;
  city: string;
  state: string;
  country: string;
  latitude: number;
  longitude: number;
  address: string;
  active: boolean;
}
