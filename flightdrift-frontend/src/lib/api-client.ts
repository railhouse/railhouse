/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import axios from "axios";
import { env } from "@/lib/env.ts";

export const apiClient = axios.create({
  baseURL: env.apiBaseUrl,
  headers: {
    "Content-Type": "application/json",
  },
});
