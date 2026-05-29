/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import { Button } from "@/components/ui/button.tsx";
import { CalendarClock, Circle, Minimize2, Plus, Signal, Tag } from "lucide-react";
import type { JSX, ReactNode } from "react";
import type { Board, BoardItem } from "@/@types/item.ts";

type BoardsProps = {
  boards: Board[];
};

type ItemBadgeProps = {
  children: ReactNode;
  label: string;
};

type BoardItemCardProps = {
  item: BoardItem;
};

const boardActionClassName = "size-4 rounded-[3px] p-0 text-muted-text hover:bg-white";

const ItemBadge = ({ children, label }: ItemBadgeProps): JSX.Element => {
  return (
    <span
      className={
        "flex shrink-0 items-center justify-center gap-1.5 rounded-[5px] " +
        "border-[0.5px] border-[#dddddd] px-2 py-1 text-[12px] " +
        "font-medium leading-none text-muted-text/80"
      }>
      {children}
      {label}
    </span>
  );
};

const BoardItemCard = ({ item }: BoardItemCardProps): JSX.Element => {
  return (
    <article className="flex w-full flex-col gap-4 rounded-[5px] bg-white px-4 py-4 cursor-default">
      <div className="flex flex-col gap-2">
        <p className="text-xs font-medium leading-none text-muted-foreground">{item.code}</p>
        <h3 className="text-[1.05rem] font-normal leading-6 text-foreground">{item.title}</h3>
      </div>

      <div className="flex flex-col gap-1.5">
        <div className="flex flex-wrap items-center gap-[9px]">
          <div className="flex items-center">
            <img
              src={item.assignee.avatarUrl}
              alt={item.assignee.name}
              className="size-[18px] rounded-full object-cover"
            />
          </div>

          <ItemBadge label={item.status}>
            <Circle size={12} strokeWidth={1.8} />
          </ItemBadge>
          <ItemBadge label={item.dateRange}>
            <CalendarClock size={12} strokeWidth={1.8} />
          </ItemBadge>
          <ItemBadge label={item.priority}>
            <Signal size={12} strokeWidth={1.8} />
          </ItemBadge>

          {item.tags.map((tag, index) => (
            <ItemBadge key={tag.name} label={tag.name}>
              <Tag
                fill={index === 0 ? "oklch(0.878 0.055 260.255)" : "oklch(0.866 0.073 15.5)"}
                size={12}
                strokeWidth={1.8}
                className={index === 0 ? "text-blue-500" : "text-destructive"}
              />
            </ItemBadge>
          ))}
        </div>
      </div>
    </article>
  );
};

const Boards = ({ boards }: BoardsProps): JSX.Element => {
  return (
    <section className="flex min-h-0 flex-1 gap-3 overflow-x-auto" aria-label="Boards">
      {boards.map((board) => (
        <div
          key={board.title}
          className={
            "flex h-full w-[384px] shrink-0 flex-col gap-4 overflow-hidden " +
            "rounded-[5px] bg-background/50 px-3 py-3.5"
          }>
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2 text-[16px] font-semibold leading-none">
              <h2 className="text-[#3b3b3b]">{board.title}</h2>
              <span className="text-muted-text">{board.items.length}</span>
            </div>
            <div className="flex items-center gap-1.5">
              <Button
                type="button"
                variant="ghost"
                size="icon-xs"
                aria-label={`Collapse ${board.title} board`}
                className={boardActionClassName}>
                <Minimize2 className="size-[15px]" strokeWidth={1.8} />
              </Button>
              <Button
                type="button"
                variant="ghost"
                size="icon-xs"
                aria-label={`Add item to ${board.title}`}
                className={boardActionClassName}>
                <Plus className="size-4" strokeWidth={1.8} />
              </Button>
            </div>
          </div>

          <div className="flex min-h-0 flex-1 flex-col gap-3 overflow-y-auto">
            {board.items.map((item) => (
              <BoardItemCard key={item.code} item={item} />
            ))}
          </div>
        </div>
      ))}
    </section>
  );
};

export default Boards;
