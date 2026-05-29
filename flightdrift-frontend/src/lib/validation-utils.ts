/*
* Author: Jamius Siam
* Since: 30/05/2026
*/
import { z } from "zod";

export const requiredText = (message: string): z.ZodString => z.string().trim().min(1, message);
