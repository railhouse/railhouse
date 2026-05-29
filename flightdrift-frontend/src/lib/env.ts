/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
export const env = {
  apiBaseUrl: import.meta.env.VITE_API_BASE_URL,
} as const;

if (!env.apiBaseUrl) {
  throw new Error("VITE_API_BASE_URL is not configured");
}
