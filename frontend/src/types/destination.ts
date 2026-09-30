export interface Destination {
  id: number;
  name: string;
  country: string;
  state: string;
  description: string;
}

export interface DestinationPayload {
  name: string;
  country: string;
  state: string;
  description: string;
}
