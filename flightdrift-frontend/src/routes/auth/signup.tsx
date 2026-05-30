/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { Link, createFileRoute } from "@tanstack/react-router";
import { useForm } from "@tanstack/react-form";
import { useMutation } from "@tanstack/react-query";
import ErrorAlert from "@/components/alerts/error-alert.tsx";
import Loader from "@/components/loader/loader.tsx";
import SuccessAlert from "@/components/alerts/success-alert.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Field, FieldError, FieldGroup, FieldLabel } from "@/components/ui/field.tsx";
import { Input } from "@/components/ui/input.tsx";
import { signup } from "@/lib/auth-api.ts";
import { signupSchema } from "@/lib/auth-schema.ts";
import { getFieldError, validateZodField } from "@/lib/form-utils.ts";
import type { SignupRequest } from "@/@types/auth.ts";
import type { JSX } from "react";
import { getApiErrorMessage } from "@/lib/utils.ts";
import { useState } from "react";

const Signup = (): JSX.Element => {
  const [successMessage, setSuccessMessage] = useState<string>();
  const signupMutation = useMutation({
    mutationFn: signup,
  });

  const form = useForm({
    defaultValues: {
      name: "",
      email: "",
      username: "",
      password: "",
    } satisfies SignupRequest,
    onSubmit: async ({ value }) => {
      setSuccessMessage(undefined);

      try {
        const response = await signupMutation.mutateAsync({
          name: value.name,
          email: value.email,
          username: value.username,
          password: value.password,
        });

        form.reset();

        setSuccessMessage(
          `User account has been successfully created for ${response.username}. Now you can log in.`,
        );
      } catch {
        setSuccessMessage(undefined);
        return;
      }
    },
  });
  const formError = signupMutation.error ? getApiErrorMessage(signupMutation.error) : undefined;

  return (
    <div className="grid gap-7">
      <img src="/flightdrift_logo_light.svg" alt="Flightdrift" className="h-5 w-fit lg:hidden" />

      <div className="grid gap-2">
        <h1 className="font-heading text-2xl font-semibold leading-tight">Create account</h1>
        <p className="text-sm text-muted-foreground">Start with your account details.</p>
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
            name="name"
            validators={{
              onChange: ({ value }) => validateZodField(value, signupSchema.shape.name),
              onSubmit: ({ value }) => validateZodField(value, signupSchema.shape.name),
            }}>
            {(field) => {
              const error = getFieldError(field.state.meta.errors);

              return (
                <Field>
                  <FieldLabel htmlFor={field.name}>Name</FieldLabel>
                  <Input
                    id={field.name}
                    name={field.name}
                    autoComplete="name"
                    value={field.state.value}
                    aria-invalid={Boolean(error)}
                    onBlur={field.handleBlur}
                    onChange={(event) => field.handleChange(event.target.value.trim())}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              );
            }}
          </form.Field>

          <form.Field
            name="email"
            validators={{
              onChange: ({ value }) => validateZodField(value, signupSchema.shape.email),
              onSubmit: ({ value }) => validateZodField(value, signupSchema.shape.email),
            }}>
            {(field) => {
              const error = getFieldError(field.state.meta.errors);

              return (
                <Field>
                  <FieldLabel htmlFor={field.name}>Email</FieldLabel>
                  <Input
                    id={field.name}
                    name={field.name}
                    type="email"
                    autoComplete="email"
                    value={field.state.value}
                    aria-invalid={Boolean(error)}
                    onBlur={field.handleBlur}
                    onChange={(event) => field.handleChange(event.target.value.trim())}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              );
            }}
          </form.Field>

          <form.Field
            name="username"
            validators={{
              onChange: ({ value }) => validateZodField(value, signupSchema.shape.username),
              onSubmit: ({ value }) => validateZodField(value, signupSchema.shape.username),
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
                    onChange={(event) => field.handleChange(event.target.value.trim())}
                  />
                  <FieldError>{error}</FieldError>
                </Field>
              );
            }}
          </form.Field>

          <form.Field
            name="password"
            validators={{
              onChange: ({ value }) => validateZodField(value, signupSchema.shape.password),
              onSubmit: ({ value }) => validateZodField(value, signupSchema.shape.password),
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
                    autoComplete="new-password"
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
        <SuccessAlert message={successMessage} show={Boolean(successMessage)} />

        <form.Subscribe selector={(state) => [state.canSubmit, state.isSubmitting] as const}>
          {([canSubmit, isSubmitting]) => (
            <Button type="submit" className="h-9 w-full" disabled={!canSubmit || isSubmitting}>
              {isSubmitting ? (
                <>
                  <Loader />
                  Signing up
                </>
              ) : (
                "Create account"
              )}
            </Button>
          )}
        </form.Subscribe>
      </form>

      <p className="text-center text-sm text-muted-foreground">
        Already have an account?{" "}
        <Link
          to="/auth/signin"
          className="font-medium text-foreground underline-offset-4 hover:underline">
          Sign in
        </Link>
      </p>
    </div>
  );
};

export const Route = createFileRoute("/auth/signup")({
  component: Signup,
});
