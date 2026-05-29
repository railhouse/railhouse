/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { z } from "zod";

export const getFieldError = (errors: unknown[]): string | undefined => {
  const [error] = errors;

  if (typeof error === "string") {
    return error;
  }

  if (error instanceof Error) {
    return error.message;
  }

  return undefined;
};

export const validateZodField = (value: string, schema: z.ZodString): string | undefined => {
  const result = schema.safeParse(value);

  return result.success ? undefined : result.error.issues[0]?.message;
};
