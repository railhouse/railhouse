/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { Button } from "@/components/ui/button.tsx";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu.tsx";
import { signout } from "@/lib/auth-api.ts";
import { useAuthStore } from "@/stores/auth-store.ts";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "@tanstack/react-router";
import { LogOut, Settings } from "lucide-react";
import type { JSX } from "react";

const menuItemClassName =
  "h-7 cursor-pointer text-[13px] font-normal hover:bg-muted focus:bg-muted " +
  "focus:text-accent-foreground data-[highlighted]:bg-muted " +
  "data-[highlighted]:text-accent-foreground";

const destructiveMenuItemClassName =
  "h-7 cursor-pointer text-[13px] font-normal hover:bg-muted focus:bg-muted " +
  "data-[highlighted]:bg-muted";

const UserMenu = (): JSX.Element => {
  const navigate = useNavigate();
  const token = useAuthStore((state) => state.token);
  const clearAuth = useAuthStore((state) => state.clearAuth);

  const signoutMutation = useMutation({
    mutationFn: async () => {
      if (!token) {
        return;
      }

      await signout(token);
    },
    onSettled: () => {
      clearAuth();
      void navigate({ to: "/auth/signin" });
    },
  });

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button type="button" variant="ghost" className={"h-9 flex-1 justify-start gap-2"}>
          <img
            src="/avatar.jpg"
            alt="Jamius Siam"
            className="size-[18px] rounded-full object-cover"
          />
          <span className="truncate text-[13px] font-normal leading-none">Jamius Siam</span>
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="rounded-[5px] border border-muted-foreground/35">
        <DropdownMenuItem className={menuItemClassName}>
          <Settings size={15} strokeWidth={1.8} />
          Settings
        </DropdownMenuItem>
        <DropdownMenuSeparator />
        <DropdownMenuItem
          variant="destructive"
          className={destructiveMenuItemClassName}
          disabled={signoutMutation.isPending}
          onSelect={(event) => {
            event.preventDefault();
            signoutMutation.mutate();
          }}>
          <LogOut size={15} strokeWidth={1.8} />
          {signoutMutation.isPending ? "Signing out" : "Sign out"}
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default UserMenu;
