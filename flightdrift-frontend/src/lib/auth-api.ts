/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { apiClient } from "@/lib/api-client.ts";
import type {
  ApiResponse,
  SigninRequest,
  SignupRequest,
  SignupResponse,
  TokenResponse,
} from "@/@types/auth.ts";

export const signin = async (request: SigninRequest): Promise<TokenResponse> => {
  const response = await apiClient.post<ApiResponse<TokenResponse>>("/api/auth/signin", request);

  if (!response.data.success || !response.data.data) {
    throw new Error(response.data.message);
  }

  return response.data.data;
};

export const signup = async (request: SignupRequest): Promise<SignupResponse> => {
  const response = await apiClient.post<ApiResponse<SignupResponse>>("/api/auth/signup", request);

  if (!response.data.success || !response.data.data) {
    throw new Error(response.data.message);
  }

  return response.data.data;
};

export const signout = async (token: string): Promise<void> => {
  await apiClient.post<ApiResponse<null>>("/api/auth/signout", null, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};
