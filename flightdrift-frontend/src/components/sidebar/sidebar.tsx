/*
 * Author: Jamius Siam
 * Since: 28/05/2026
 */
import NavMenuItem from "@/components/sidebar/nav-menu-item.tsx";
import OrganizationSelector from "@/components/sidebar/organization-selector.tsx";
import UserMenu from "@/components/sidebar/user-menu.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Link } from "@tanstack/react-router";
import {
  BookmarkCheck,
  FilePlusCorner,
  Files,
  Flag,
  Folder,
  House,
  Inbox,
  ListFilterPlus,
  NotepadText,
  Sparkles,
} from "lucide-react";
import type { JSX } from "react";

const mainMenu = [
  {
    text: "Inbox",
    icon: <Inbox size={16} strokeWidth={1.8} className="relative bottom-[0.5px]" />,
  },
  {
    text: "Home",
    icon: <House size={16} strokeWidth={1.8} className="relative bottom-[0.5px]" />,
    active: true,
  },
  {
    text: "My Items",
    icon: <Files size={16} strokeWidth={1.8} />,
  },
  {
    text: "Bookmarks",
    icon: <BookmarkCheck size={16} strokeWidth={1.8} className="relative bottom-px" />,
  },
];

const projects = [
  {
    name: "artemis",
    iconUrl: "/project.webp",
    menus: [
      {
        text: "Wiki",
        icon: <NotepadText size={16} strokeWidth={1.8} />,
      },
      {
        text: "Items",
        icon: <Folder size={16} strokeWidth={1.8} />,
      },
      {
        text: "Milestone",
        icon: <Flag size={16} strokeWidth={1.8} />,
      },
    ],
  },
];

const Sidebar = (): JSX.Element => {
  return (
    <aside
      className={
        "w-[240px] shrink-0 drop-shadow-md drop-shadow-black/2 " +
        "flex flex-col justify-between rounded-lg ps-2.5 pe-2 pt-4 pb-2"
      }>
      <div className="flex flex-col gap-3">
        <div className="flex flex-col gap-3">
          <Link to="/" className="block mb-5">
            <img className="h-4" src="/flightdrift_logo_light.svg" alt="Flightdrift logo" />
          </Link>

          <div className="flex flex-row gap-2">
            <OrganizationSelector />
            <NavMenuItem
              className={
                "bg-[#3BB1FF] text-white shadow-[0_3px_4px_rgba(59,176,255,0.15)] " +
                "hover:bg-[#3BB1FF] hover:text-white w-[28px]"
              }>
              <FilePlusCorner size={14} strokeWidth={1.8} />
            </NavMenuItem>
          </div>

          <div className="flex flex-col gap-2">
            <nav className="flex flex-col" aria-label="Main menu">
              {mainMenu.map((item) => (
                <NavMenuItem key={item.text} active={item.active} className="text-muted-text">
                  {item.icon}
                  {item.text}
                </NavMenuItem>
              ))}
            </nav>
          </div>
        </div>

        <section>
          <div className="mb-2 flex items-center justify-between">
            <h2 className="text-[11px] font-medium leading-none text-foreground">Projects</h2>
            <Button type="button" variant="ghost" size="icon-sm" aria-label="Filter projects">
              <ListFilterPlus size={14} strokeWidth={2} className="text-foreground" />
            </Button>
          </div>

          <div className="flex flex-col gap-0">
            {projects.map((project) => (
              <div key={project.name} className="flex flex-col gap-1">
                <NavMenuItem
                  variant={"outline"}
                  className={"border-dashed text-muted-text border-muted-foreground/50"}>
                  <img
                    src={project.iconUrl}
                    alt={project.name}
                    className="size-[15px] rounded-[3px] object-contain"
                  />
                  {project.name}
                </NavMenuItem>

                <nav
                  className="ms-2 flex flex-col border-l border-dashed border-muted-foreground/50 ps-2"
                  aria-label={`${project.name} project menu`}>
                  {project.menus.map((menu) => (
                    <NavMenuItem key={menu.text} className="text-muted-text">
                      {menu.icon}
                      {menu.text}
                    </NavMenuItem>
                  ))}
                </nav>
              </div>
            ))}
          </div>
        </section>
      </div>

      <div className="flex flex-col gap-2">
        <div className="h-[0.5px] w-full bg-muted-foreground/20" />

        <div className="flex items-center gap-2">
          <UserMenu />

          <Button
            type="button"
            variant="ghost"
            size="icon"
            aria-label="Open AI actions"
            className={
              "size-7 rounded-full bg-linear-60 from-[#d400ff] to-[#fe6c6c] text-white " +
              "hover:from-[#de38ff] hover:to-[#ff6161] hover:text-white transition-colors duration-300"
            }>
            <Sparkles
              strokeWidth={1.8}
              size={18}
              className="text-white group-hover/button:rotate-15 group-hover/button:scale-105 transition-transform duration-150"
            />
          </Button>
        </div>
      </div>
    </aside>
  );
};

export default Sidebar;
