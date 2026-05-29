import * as React from "react";
import { Label } from "@/components/ui/label.tsx";
import { cn } from "@/lib/utils.ts";
import type { JSX } from "react";

const FieldGroup = ({ className, ...props }: React.ComponentProps<"div">): JSX.Element => {
  return <div data-slot="field-group" className={cn("grid gap-4", className)} {...props} />;
};

const Field = ({ className, ...props }: React.ComponentProps<"div">): JSX.Element => {
  return <div data-slot="field" className={cn("grid gap-2", className)} {...props} />;
};

const FieldLabel = ({ className, ...props }: React.ComponentProps<typeof Label>): JSX.Element => {
  return <Label data-slot="field-label" className={cn("text-[13px]", className)} {...props} />;
};

const FieldError = ({
  className,
  children,
  ...props
}: React.ComponentProps<"p">): JSX.Element | null => {
  if (!children) {
    return null;
  }

  return (
    <p data-slot="field-error" className={cn("text-xs text-destructive", className)} {...props}>
      {children}
    </p>
  );
};

export { Field, FieldError, FieldGroup, FieldLabel };
