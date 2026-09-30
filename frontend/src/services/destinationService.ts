import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { Destination, DestinationPayload } from "@/types/destination";

export const destinationService = {
  async getAll(): Promise<Destination[]> {
    console.log("[destinationService] getAll - URL:", ENDPOINTS.DESTINATIONS.ROOT);
    const { data } = await api.get<Destination[]>(ENDPOINTS.DESTINATIONS.ROOT);
    console.log("[destinationService] getAll - Response:", data);
    return data;
  },

  async search(searchTerm: string): Promise<Destination[]> {
    const url = ENDPOINTS.DESTINATIONS.ROOT;
    const params = { search: searchTerm };
    console.log("[destinationService] search - URL:", url, "Params:", params);
    
    const { data } = await api.get<Destination[]>(url, { params });
    console.log("[destinationService] search - Response:", data);
    console.log("[destinationService] search - Response length:", data?.length);
    console.log("[destinationService] search - Response type:", typeof data, Array.isArray(data));
    
    return data;
  },

  async getById(id: number | string): Promise<Destination> {
    const { data } = await api.get<Destination>(ENDPOINTS.DESTINATIONS.BY_ID(id));
    return data;
  },

  async create(payload: DestinationPayload): Promise<Destination> {
    const { data} = await api.post<Destination>(ENDPOINTS.DESTINATIONS.ROOT, payload);
    return data;
  },

  async update(id: number | string, payload: DestinationPayload): Promise<Destination> {
    const { data } = await api.put<Destination>(ENDPOINTS.DESTINATIONS.BY_ID(id), payload);
    return data;
  },

  async remove(id: number | string): Promise<void> {
    await api.delete(ENDPOINTS.DESTINATIONS.BY_ID(id));
  },
};
