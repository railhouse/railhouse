/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { Button } from "@/components/ui/button.tsx";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu.tsx";
import { ChevronDown } from "lucide-react";
import type { JSX } from "react";

const OrganizationSelector = (): JSX.Element => {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          type="button"
          variant="outline"
          className={
            "px-2 py-4 h-6 flex-1 justify-between rounded-[5px] text-[13px] font-normal " +
            "text-foreground hover:bg-white border-muted-foreground/35 " +
            "aria-expanded:bg-white aria-expanded:hover:bg-white " +
            "data-[state=open]:bg-white data-[state=open]:hover:bg-white"
          }>
          <span className="flex items-center gap-1.75">
            <img
              src="/organization_icon.png"
              alt="Sable Order"
              className="size-[15px] rounded-[3px] object-cover"
            />
            <span className="text-[13px] font-normal leading-none text-foreground">
              Sable Order
            </span>
          </span>
          <ChevronDown size={15} strokeWidth={1.8} className="text-foreground" />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="rounded-[5px] border border-muted-foreground/35">
        <DropdownMenuItem className="gap-1.75 h-6 text-[13px] font-normal focus:bg-muted focus:text-accent-foreground">
          <img
            src="/organization_icon.png"
            alt="Sable Order"
            className="size-[15px] rounded-[3px] object-cover"
          />
          Sable Order
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default OrganizationSelector;
