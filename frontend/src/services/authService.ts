import api from "./api";
import { ENDPOINTS } from "./endpoints";
import type { AuthResponse, LoginRequest, SignupRequest, User } from "@/types/auth";

export const authService = {
  async login(payload: LoginRequest): Promise<AuthResponse> {
    const { data } = await api.post<AuthResponse>(ENDPOINTS.AUTH.LOGIN, payload);
    return data;
  },

  async signup(payload: SignupRequest): Promise<AuthResponse> {
    const { data } = await api.post<AuthResponse>(ENDPOINTS.AUTH.SIGNUP, payload);
    return data;
  },

  async me(): Promise<User> {
    const { data } = await api.get<User>(ENDPOINTS.AUTH.ME);
    return data;
  },

  async logout(): Promise<void> {
    await api.post(ENDPOINTS.AUTH.LOGOUT);
  },
};
