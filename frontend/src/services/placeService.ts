import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { Place, PlacePayload } from "@/types/place";

export const placeService = {
  async getAll(): Promise<Place[]> {
    const { data } = await api.get<Place[]>(ENDPOINTS.PLACES.ROOT);
    return data;
  },

  async getActive(): Promise<Place[]> {
    const { data } = await api.get<Place[]>(ENDPOINTS.PLACES.ACTIVE);
    return data;
  },

  async getById(id: number | string): Promise<Place> {
    const { data } = await api.get<Place>(ENDPOINTS.PLACES.BY_ID(id));
    return data;
  },

  async getByCity(city: string): Promise<Place[]> {
    const { data } = await api.get<Place[]>(ENDPOINTS.PLACES.BY_CITY(city));
    return data;
  },

  async getByCountry(country: string): Promise<Place[]> {
    const { data } = await api.get<Place[]>(ENDPOINTS.PLACES.BY_COUNTRY(country));
    return data;
  },

  async create(payload: PlacePayload): Promise<Place> {
    const { data } = await api.post<Place>(ENDPOINTS.PLACES.ROOT, payload);
    return data;
  },

  async update(id: number | string, payload: PlacePayload): Promise<Place> {
    const { data } = await api.put<Place>(ENDPOINTS.PLACES.BY_ID(id), payload);
    return data;
  },

  async remove(id: number | string): Promise<void> {
    await api.delete(ENDPOINTS.PLACES.BY_ID(id));
  },
};
