export interface ItineraryItem {
  id: number;
  tripId: number;
  title: string;
  description: string;
  day: number;
  startTime?: string;
  placeId?: number;
}

export interface Trip {
  id: number;
  title: string;
  destination: string;
  startDate: string;
  endDate: string;
  description: string;
  active: boolean;
  itinerary?: ItineraryItem[];
}

export interface TripPayload {
  title: string;
  destination: string;
  startDate: string;
  endDate: string;
  description: string;
}
