/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import type { JSX } from "react";

type SuccessAlertProps = {
  message?: string;
  show: boolean;
};

const SuccessAlert = ({ message, show }: SuccessAlertProps): JSX.Element | null => {
  if (!show || !message) {
    return null;
  }

  return (
    <p role="alert"
       aria-live="polite"
       className="rounded-md border border-emerald-500/30 bg-emerald-500/5 px-3 py-2 text-xs text-emerald-700">
      {message}
    </p>
  );
};

export default SuccessAlert;
