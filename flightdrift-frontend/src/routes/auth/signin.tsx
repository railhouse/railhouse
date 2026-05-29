/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { Link, createFileRoute, useNavigate } from "@tanstack/react-router";
import { useForm } from "@tanstack/react-form";
import { useMutation } from "@tanstack/react-query";
import ErrorAlert from "@/components/alerts/error-alert.tsx";
import Loader from "@/components/loader/loader.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Field, FieldError, FieldGroup, FieldLabel } from "@/components/ui/field.tsx";
import { Input } from "@/components/ui/input.tsx";
import { signin } from "@/lib/auth-api.ts";
import { signinSchema } from "@/lib/auth-schema.ts";
import { getFieldError, validateZodField } from "@/lib/form-utils.ts";
import { useAuthStore } from "@/stores/auth-store.ts";
import type { SigninRequest } from "@/@types/auth.ts";
import type { JSX } from "react";
import { getApiErrorMessage } from "@/lib/utils.ts";

const Signin = (): JSX.Element => {
  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);
  const signinMutation = useMutation({
    mutationFn: signin,
  });

  const form = useForm({
    defaultValues: {
      username: "",
      password: "",
    } satisfies SigninRequest,
    onSubmit: async ({ value }) => {
      try {
        const response = await signinMutation.mutateAsync({
          username: value.username.trim(),
          password: value.password,
        });

        setAuth(response.token, response.userInfo);
        await navigate({ to: "/dash/items" });
      } catch {
        return;
      }
    },
  });

  const formError = signinMutation.error ? getApiErrorMessage(signinMutation.error) : undefined;

  return (
    <div className="grid gap-7">
      <img src="/flightdrift_logo_light.svg" alt="Flightdrift" className="h-5 w-fit lg:hidden" />

      <div className="grid gap-2">
        <h1 className="font-heading text-2xl font-semibold leading-tight">Sign in</h1>
        <p className="text-sm text-muted-foreground">Enter your account details to continue.</p>
      </div>

      <form
        className="grid gap-5"
        onSubmit={(event) => {
          event.preventDefault();
          event.stopPropagation();
          void form.handleSubmit();
        }}>
        <FieldGroup>
          <form.Field
            name="username"
            validators={{
              onChange: ({ value }) => validateZodField(value, signinSchema.shape.username),
              onSubmit: ({ value }) => validateZodField(value, signinSchema.shape.username),
            }}>
            {(field) => {
              const error = getFieldError(field.state.meta.errors);

              return (
                <Field>
                  <FieldLabel htmlFor={field.name}>Username</FieldLabel>
                  <Input
                    id={field.name}
                    name={field.name}
                    autoComplete="username"
                    value={field.state.value}
                    aria-invalid={Boolean(error)}
                    onBlur={field.handleBlur}
                    onChange={(event) => field.handleChange(event.target.value)}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              );
            }}
          </form.Field>

          <form.Field
            name="password"
            validators={{
              onChange: ({ value }) => validateZodField(value, signinSchema.shape.password),
              onSubmit: ({ value }) => validateZodField(value, signinSchema.shape.password),
            }}>
            {(field) => {
              const error = getFieldError(field.state.meta.errors);

              return (
                <Field>
                  <FieldLabel htmlFor={field.name}>Password</FieldLabel>
                  <Input
                    id={field.name}
                    name={field.name}
                    type="password"
                    autoComplete="current-password"
                    value={field.state.value}
                    aria-invalid={Boolean(error)}
                    onBlur={field.handleBlur}
                    onChange={(event) => field.handleChange(event.target.value)}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              );
            }}
          </form.Field>
        </FieldGroup>

        <ErrorAlert message={formError} show={Boolean(formError)} />

        <form.Subscribe selector={(state) => [state.canSubmit, state.isSubmitting] as const}>
          {([canSubmit, isSubmitting]) => (
            <Button type="submit" className="h-9 w-full" disabled={!canSubmit || isSubmitting}>
              {isSubmitting ? (
                <>
                  <Loader />
                  Signing in
                </>
              ) : (
                "Sign in"
              )}
            </Button>
          )}
        </form.Subscribe>
      </form>

      <p className="text-center text-sm text-muted-foreground">
        Need an account?{" "}
        <Link
          to="/auth/signup"
          className="font-medium text-foreground underline-offset-4 hover:underline">
          Sign up
        </Link>
      </p>
    </div>
  );
};

export const Route = createFileRoute("/auth/signin")({
  component: Signin,
});
