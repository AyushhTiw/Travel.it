export interface User {
  userId: number;
  name: string;
  email: string;
  role: string;
  profilePicture?: string | null;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  userId: number;
  name: string;
  email: string;
  role: string;
  profilePicture?: string | null;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
}

export interface RefreshRequest {
  refreshToken: string;
}

export interface RefreshResponse {
  accessToken: string;
  refreshToken?: string;
}
