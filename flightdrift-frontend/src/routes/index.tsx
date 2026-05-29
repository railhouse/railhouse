/*
 * Author: Jamius Siam
 * Since: 06/05/2026
 */
import { createFileRoute, redirect } from "@tanstack/react-router";
import { useAuthStore } from "@/stores/auth-store.ts";

export const Route = createFileRoute("/")({
  beforeLoad: () => {
    const authState = useAuthStore.getState();

    if (authState.isAuthenticated()) {
      throw redirect({ to: "/dash/items" });
    }

    if (authState.token) {
      authState.clearAuth();
    }

    throw redirect({ to: "/auth/signin" });
  },
});
