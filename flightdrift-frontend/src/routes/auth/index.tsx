/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/auth/")({
  beforeLoad: () => {
    throw redirect({ to: "/auth/signin" });
  },
});
