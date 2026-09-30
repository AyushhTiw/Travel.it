import axios, {
  AxiosError,
  AxiosHeaders,
  type AxiosInstance,
  type InternalAxiosRequestConfig,
} from "axios";

import { AUTH_FREE_PATHS, ENDPOINTS } from "./endpoints";
import { clearTokens, getAccessToken, getRefreshToken, setTokens } from "@/utils/tokenStorage";
import type { RefreshResponse } from "@/types/auth";

const baseURL = import.meta.env['VITE_API_BASE_URL'] ?? "http://localhost:8080/api/v1";

type RetriableConfig = InternalAxiosRequestConfig & { _retried?: boolean };

export const api: AxiosInstance = axios.create({
  baseURL,
  headers: { "Content-Type": "application/json" },
});

/** Notifies the app (AuthContext) that the session can no longer be recovered. */
type SessionExpiredHandler = () => void;
let onSessionExpired: SessionExpiredHandler | null = null;

export function setSessionExpiredHandler(handler: SessionExpiredHandler | null): void {
  onSessionExpired = handler;
}

function isAuthFreePath(url?: string): boolean {
  if (!url) return false;
  return AUTH_FREE_PATHS.some((path) => url.startsWith(path));
}

api.interceptors.request.use((config) => {
  const token = getAccessToken();
  const headers = AxiosHeaders.from(config.headers);
  if (token && !isAuthFreePath(config.url)) {
    headers.set("Authorization", `Bearer ${token}`);
  } else {
    headers.delete("Authorization");
  }
  config.headers = headers;
  return config;
});

// Single-flight refresh: concurrent 401s share one refresh request.
let refreshPromise: Promise<string> | null = null;

async function refreshAccessToken(): Promise<string> {
  const refreshToken = getRefreshToken();
  if (!refreshToken) throw new Error("No refresh token available.");

  const response = await axios.post<RefreshResponse>(
    `${baseURL}${ENDPOINTS.AUTH.REFRESH}`,
    { refreshToken },
    { headers: { "Content-Type": "application/json" } },
  );

  const { accessToken, refreshToken: nextRefreshToken } = response.data;
  if (!accessToken) throw new Error("Refresh response did not contain an access token.");
  setTokens(accessToken, nextRefreshToken ?? refreshToken);
  return accessToken;
}

function handleSessionExpired(): void {
  clearTokens();
  onSessionExpired?.();
}

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const config = error.config as RetriableConfig | undefined;
    const status = error.response?.status;

    if (status !== 401 || !config || config._retried || isAuthFreePath(config.url)) {
      return Promise.reject(error);
    }

    if (!getRefreshToken()) {
      handleSessionExpired();
      return Promise.reject(error);
    }

    config._retried = true;

    try {
      refreshPromise = refreshPromise ?? refreshAccessToken();
      const accessToken = await refreshPromise;
      refreshPromise = null;
      const headers = AxiosHeaders.from(config.headers);
      headers.set("Authorization", `Bearer ${accessToken}`);
      config.headers = headers;
      return api.request(config);
    } catch (refreshError) {
      refreshPromise = null;
      handleSessionExpired();
      return Promise.reject(refreshError);
    }
  },
);

/** Turns any thrown error into a message that is safe to show a traveller. */
export function toFriendlyMessage(error: unknown, fallback = "Something went wrong. Please try again."): string {
  if (axios.isAxiosError(error)) {
    const status = error.response?.status;
    if (status === 401) return "Your session has expired. Please login again.";
    if (status === 403) return "You do not have access to this.";
    if (status === 404) return "We couldn't find what you were looking for.";
    if (!error.response) return "We couldn't reach the server. Check your connection and try again.";
    const data = error.response.data as { message?: unknown } | undefined;
    if (data && typeof data.message === "string" && data.message.length < 140) return data.message;
    return fallback;
  }
  if (error instanceof Error && error.message && error.message.length < 140) return error.message;
  return fallback;
}

export default api;
