import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { Coordinates, NearbyPlace, NearbySearchQuery } from "@/types/explore";

export const exploreService = {
  async getNearby(query: NearbySearchQuery): Promise<NearbyPlace[]> {
    const { data } = await api.get<NearbyPlace[]>(ENDPOINTS.EXPLORE.NEARBY, {
      params: {
        latitude: query.latitude,
        longitude: query.longitude,
        radiusKm: query.radiusKm,
      },
    });
    return data;
  },
};
