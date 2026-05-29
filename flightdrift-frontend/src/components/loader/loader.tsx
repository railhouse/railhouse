/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { cn } from "@/lib/utils.ts";
import { LoaderCircle } from "lucide-react";
import type { JSX } from "react";

type LoaderProps = {
  className?: string;
};

const Loader = ({ className }: LoaderProps): JSX.Element => {
  return (
    <LoaderCircle
      aria-hidden="true"
      className={cn("size-3.5 animate-spin relative bottom-[0.5px]", className)}
      strokeWidth={2}
    />
  );
};

export default Loader;
