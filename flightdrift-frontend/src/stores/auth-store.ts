/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { create } from "zustand";
import { persist } from "zustand/middleware";
import { jwtDecode, type JwtPayload } from "jwt-decode";
import type { Account } from "@/@types/auth.ts";

type AuthState = {
  token: string | null;
  account: Account | null;
  setAuth: (token: string, account: Account) => void;
  clearAuth: () => void;
  isAuthenticated: () => boolean;
};

const isTokenValid = (token: string): boolean => {
  try {
    const payload = jwtDecode<JwtPayload>(token);

    if (!payload.exp) {
      return false;
    }

    return payload.exp * 1000 > Date.now();
  } catch {
    return false;
  }
};

export const useAuthStore = create<AuthState>()(
  persist(
    (set, get) => ({
      token: null,
      account: null,
      setAuth: (token, account): void => {
        set({ token, account });
      },
      clearAuth: (): void => {
        set({ token: null, account: null });
      },
      isAuthenticated: (): boolean => {
        const { token, account } = get();

        return token !== null && account !== null && isTokenValid(token);
      },
    }),
    {
      name: "flightdrift-auth",
      partialize: (state) => ({
        token: state.token,
        account: state.account,
      }),
    },
  ),
);
