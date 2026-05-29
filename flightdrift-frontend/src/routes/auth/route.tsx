/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { createFileRoute, Outlet, redirect } from "@tanstack/react-router";
import { useAuthStore } from "@/stores/auth-store.ts";
import type { JSX } from "react";

// TODO: Replace with a nice graphic
const AuthLayout = (): JSX.Element => {
  return (
    <div className="grid min-h-screen grid-cols-1 bg-background lg:grid-cols-[3fr_2fr]">
      <section className="hidden min-h-screen overflow-hidden bg-[#eef7fb] p-8 lg:flex">
        <div className="flex h-full w-full flex-col justify-between">
          <img src="/flightdrift_logo_light.svg" alt="Flightdrift" className="h-5 w-fit" />

          <div className="mx-auto grid w-full max-w-[680px] gap-5">
            <div className="flex items-center justify-between rounded-lg border border-white bg-white/80 p-4 shadow-xl shadow-[#8ac9dc]/20">
              <div>
                <p className="text-xs font-medium text-muted-foreground">artemis</p>
                <h1 className="font-heading text-2xl font-semibold leading-tight text-foreground">
                  Launch board
                </h1>
              </div>
              <div className="flex -space-x-2">
                <img
                  src="/avatar.jpg"
                  alt="Jamius Siam"
                  className="size-8 rounded-full border-2 border-white"
                />
                <img
                  src="/project.webp"
                  alt="Project"
                  className="size-8 rounded-full border-2 border-white object-cover"
                />
              </div>
            </div>

            <div className="grid grid-cols-3 gap-3">
              {["Todo", "In Progress", "Done"].map((status, statusIndex) => (
                <div
                  key={status}
                  className="min-h-[250px] rounded-lg border border-white bg-white/70 p-3 shadow-lg shadow-[#8ac9dc]/10">
                  <div className="mb-3 flex items-center justify-between">
                    <span className="text-xs font-medium text-foreground">{status}</span>
                    <span className="size-1.5 rounded-full bg-[#3bb1ff]" />
                  </div>

                  {[0, 1, 2].map((itemIndex) => (
                    <div
                      key={`${status}-${itemIndex}`}
                      className="mb-2 rounded-md border border-border bg-white p-3 shadow-sm">
                      <div
                        className={
                          "mb-2 h-1.5 rounded-full " +
                          ["bg-[#3bb1ff]", "bg-[#2fc18d]", "bg-[#f3b341]"][statusIndex]
                        }
                      />
                      <div className="mb-2 h-2 rounded-full bg-muted" />
                      <div className="h-2 w-2/3 rounded-full bg-muted" />
                    </div>
                  ))}
                </div>
              ))}
            </div>
          </div>

          <p className="text-xs text-muted-foreground cursor-default hover:text-muted-text transition-colors duration-200">
            flightdrift.com
          </p>
        </div>
      </section>

      <main className="flex min-h-screen items-center justify-center bg-white px-6 py-8 lg:px-12">
        <div className="w-full max-w-[380px]">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export const Route = createFileRoute("/auth")({
  beforeLoad: () => {
    const authState = useAuthStore.getState();

    if (authState.isAuthenticated()) {
      throw redirect({ to: "/dash/items" });
    }

    if (authState.token) {
      authState.clearAuth();
    }
  },
  component: AuthLayout,
});
