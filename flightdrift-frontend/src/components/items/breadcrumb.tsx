/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { ChevronRight, Folder } from "lucide-react";
import type { JSX } from "react";

const Breadcrumb = (): JSX.Element => {
  return (
    <nav aria-label="Breadcrumb" className="flex h-4 items-center gap-2 text-muted-text">
      <span className="flex items-center gap-1.5 text-[13px] font-medium leading-none">
        <img src="/project.webp" alt="artemis" className="size-[14px] rounded-[3px] object-cover" />
        artemis
      </span>
      <ChevronRight size={15} strokeWidth={1.8} />
      <span className="flex items-center gap-1.5 text-[13px] font-medium leading-none">
        <Folder size={15} strokeWidth={1.8} />
        Items
      </span>
    </nav>
  );
};

export default Breadcrumb;
