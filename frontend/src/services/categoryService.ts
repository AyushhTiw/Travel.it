import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { Category } from "@/types/category";

export const categoryService = {
  async getAll(): Promise<Category[]> {
    const { data } = await api.get<Category[]>(ENDPOINTS.CATEGORIES.ROOT);
    return data;
  },

  async getActive(): Promise<Category[]> {
    const { data } = await api.get<Category[]>(ENDPOINTS.CATEGORIES.ACTIVE);
    return data;
  },

  async getById(id: number | string): Promise<Category> {
    const { data } = await api.get<Category>(ENDPOINTS.CATEGORIES.BY_ID(id));
    return data;
  },
};
