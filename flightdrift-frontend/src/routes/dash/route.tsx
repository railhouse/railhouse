/*
 * Author: Jamius Siam
 * Since: 28/05/2026
 */
import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";
import Sidebar from "@/components/sidebar/sidebar.tsx";
import { useAuthStore } from "@/stores/auth-store.ts";
import type { JSX } from "react";

const DashboardLayout = (): JSX.Element => {
  return (
    <div className="flex gap-3 p-3 w-full h-screen">
      <Sidebar />
      <main className="bg-white flex-1 drop-shadow-md drop-shadow-black/2 rounded-lg p-4">
        <Outlet />
      </main>
    </div>
  );
};

export const Route = createFileRoute("/dash")({
  beforeLoad: () => {
    const authState = useAuthStore.getState();

    if (authState.isAuthenticated()) {
      return;
    }

    if (authState.token) {
      authState.clearAuth();
    }

    throw redirect({ to: "/auth/signin" });
  },
  component: DashboardLayout,
});
