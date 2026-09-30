import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { Trip, TripPayload } from "@/types/trip";

export const TRIP_BACKEND_READY = true;

export const tripService = {
  async getAll(): Promise<Trip[]> {
    const { data } = await api.get<Trip[]>(ENDPOINTS.TRIPS.ROOT);
    return data;
  },

  async getById(id: number | string): Promise<Trip> {
    const { data } = await api.get<Trip>(ENDPOINTS.TRIPS.BY_ID(id));
    return data;
  },

  async create(payload: TripPayload): Promise<Trip> {
    const { data } = await api.post<Trip>(ENDPOINTS.TRIPS.ROOT, payload);
    return data;
  },

  async update(id: number | string, payload: TripPayload): Promise<Trip> {
    const { data } = await api.put<Trip>(ENDPOINTS.TRIPS.BY_ID(id), payload);
    return data;
  },

  async remove(id: number | string): Promise<void> {
    await api.delete(ENDPOINTS.TRIPS.BY_ID(id));
  },
};
