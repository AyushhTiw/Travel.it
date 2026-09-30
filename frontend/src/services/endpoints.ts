/**
 * Every backend path lives here so endpoints can change in one place.
 */
export const ENDPOINTS = {
  AUTH: {
    SIGNUP: "/auth/signup",
    LOGIN: "/auth/login",
    REFRESH: "/auth/refresh",
    ME: "/auth/me",
    LOGOUT: "/auth/logout",
  },
  DESTINATIONS: {
    ROOT: "/destinations",
    BY_ID: (id: number | string) => `/destinations/${id}`,
  },
  CATEGORIES: {
    ROOT: "/categories",
    ACTIVE: "/categories/active",
    BY_ID: (id: number | string) => `/categories/${id}`,
  },
    PLACES: {
    ROOT: "/places",
    SEARCH: "/places/search",
    ACTIVE: "/places/active",
    BY_ID: (id: number | string) => `/places/${id}`,
    BY_CITY: (city: string) => `/places/city/${encodeURIComponent(city)}`,
    BY_COUNTRY: (country: string) => `/places/country/${encodeURIComponent(country)}`,
  },
  EXPLORE: {
    NEARBY: "/explore/nearby",
  },
  GOOGLE_PLACES: {
    TEXT_SEARCH: "/places/google-search",
    NEARBY_SEARCH: "/places/nearby",
    DETAILS: (placeId: string) => `/places/google-details/${encodeURIComponent(placeId)}`,
  },
  AI: {
    CONVERSATIONS: "/ai/conversations",
    CONVERSATION_BY_ID: (id: number | string) => `/ai/conversations/${id}`,
    MESSAGES: (conversationId: number | string) => `/ai/conversations/${conversationId}/messages`,
    GUEST_CHAT: "/ai/chat",
  },
  TRIPS: {
    ROOT: "/trips",
    BY_ID: (id: number | string) => `/trips/${id}`,
  },
  BUDGETS: {
    ROOT: "/budgets",
    BY_ID: (id: number | string) => `/budgets/${id}`,
    ITEMS: (budgetId: number | string) => `/budgets/${budgetId}/items`,
    ITEM: (budgetId: number | string, itemId: number | string) =>
      `/budgets/${budgetId}/items/${itemId}`,
  },
} as const;

export const AUTH_FREE_PATHS: readonly string[] = [
  ENDPOINTS.AUTH.LOGIN,
  ENDPOINTS.AUTH.SIGNUP,
  ENDPOINTS.AUTH.REFRESH,
  ENDPOINTS.AI.GUEST_CHAT,
  ENDPOINTS.EXPLORE.NEARBY,
];


