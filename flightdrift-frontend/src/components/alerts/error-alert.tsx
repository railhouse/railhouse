/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import type { JSX } from "react";

type ErrorAlertProps = {
  message?: string;
  show: boolean;
};

const ErrorAlert = ({ message, show }: ErrorAlertProps): JSX.Element | null => {
  if (!show || !message) {
    return null;
  }

  return (
    <p
      role="alert"
      aria-live="assertive"
      className="rounded-md border border-destructive/30 bg-destructive/5 px-3 py-2 text-xs text-destructive">
      {message}
    </p>
  );
};

export default ErrorAlert;
