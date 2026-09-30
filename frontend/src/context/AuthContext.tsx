import { createContext, useCallback, useEffect, useMemo, useRef, useState, type ReactNode } from "react";

import { authService } from "@/services/authService";
import { setSessionExpiredHandler } from "@/services/api";
import { clearTokens, getAccessToken, getRefreshToken, setTokens } from "@/utils/tokenStorage";
import type { AuthResponse, LoginRequest, SignupRequest, User } from "@/types/auth";

export interface AuthContextValue {
  user: User | null;
  accessToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (payload: LoginRequest) => Promise<User>;
  signup: (payload: SignupRequest) => Promise<User>;
  logout: () => Promise<void>;
  refreshUser: () => Promise<void>;
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function toUser(response: AuthResponse): User {
  return {
    userId: response.userId,
    name: response.name,
    email: response.email,
    role: response.role,
  };
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const initialised = useRef(false);

  const clearSession = useCallback(() => {
    clearTokens();
    setUser(null);
    setAccessToken(null);
  }, []);

  useEffect(() => {
    setSessionExpiredHandler(() => {
      setUser(null);
      setAccessToken(null);
    });
    return () => setSessionExpiredHandler(null);
  }, []);

  useEffect(() => {
    if (initialised.current) return;
    initialised.current = true;

    const bootstrap = async () => {
      // Skip auth check during SSR
      if (typeof window === 'undefined') {
        setIsLoading(false);
        return;
      }

      const storedAccess = getAccessToken();
      const storedRefresh = getRefreshToken();

      if (!storedAccess && !storedRefresh) {
        setIsLoading(false);
        return;
      }

      setAccessToken(storedAccess);
      try {
        // The axios interceptor transparently refreshes on 401 and retries once.
        const me = await authService.me();
        setUser(me);
        setAccessToken(getAccessToken());
      } catch {
        clearSession();
      } finally {
        setIsLoading(false);
      }
    };

    void bootstrap();
  }, [clearSession]);

  const login = useCallback(async (payload: LoginRequest): Promise<User> => {
    const response = await authService.login(payload);
    setTokens(response.accessToken, response.refreshToken);
    setAccessToken(response.accessToken);
    const nextUser = toUser(response);
    setUser(nextUser);
    return nextUser;
  }, []);

  const signup = useCallback(async (payload: SignupRequest): Promise<User> => {
    const response = await authService.signup(payload);
    setTokens(response.accessToken, response.refreshToken);
    setAccessToken(response.accessToken);
    const nextUser = toUser(response);
    setUser(nextUser);
    return nextUser;
  }, []);

  const logout = useCallback(async (): Promise<void> => {
    try {
      if (getAccessToken()) await authService.logout();
    } catch {
      // Signing out locally must succeed even if the backend call fails.
    } finally {
      clearSession();
    }
  }, [clearSession]);

  const refreshUser = useCallback(async (): Promise<void> => {
    if (!getAccessToken() && !getRefreshToken()) return;
    try {
      const me = await authService.me();
      setUser(me);
      setAccessToken(getAccessToken());
    } catch {
      clearSession();
    }
  }, [clearSession]);

  const value = useMemo<AuthContextValue>(
    () => ({
      user,
      accessToken,
      isAuthenticated: Boolean(user),
      isLoading,
      login,
      signup,
      logout,
      refreshUser,
    }),
    [user, accessToken, isLoading, login, signup, logout, refreshUser],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
