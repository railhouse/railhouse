/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import type { SigninRequest, SignupRequest } from "@/@types/auth.ts";
import { z } from "zod";
import { requiredText } from "@/lib/validation-utils.ts";

export const signinSchema = z.object({
  username: requiredText("Username cannot be empty"),
  password: requiredText("Password cannot be empty"),
}) satisfies z.ZodType<SigninRequest>;

export const signupSchema = z.object({
  name: requiredText("Name cannot be empty"),
  email: requiredText("Email cannot be empty"),
  username: requiredText("Username cannot be empty"),
  password: requiredText("Password cannot be empty"),
  invitationCode: z.string().optional(),
}) satisfies z.ZodType<SignupRequest>;
