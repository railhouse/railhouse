/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
import Boards from "@/components/items/boards.tsx";
import Breadcrumb from "@/components/items/breadcrumb.tsx";
import HorizontalRule from "@/components/items/horizontal-rule.tsx";
import { createFileRoute } from "@tanstack/react-router";
import type { JSX } from "react";
import type { Board } from "@/@types/item.ts";

const boards: Board[] = [
  {
    title: "Todo",
    items: [
      {
        code: "ARTEMIS-1",
        title: "Make the inside of the box and normal slope tileset dark and improve the shading",
        assignee: {
          name: "Jamius Siam",
          avatarUrl: "/avatar.jpg",
        },
        status: "Todo",
        priority: "High",
        dateRange: "May 22 - May 26, 2026",
        tags: [
          {
            name: "dev",
          },
          {
            name: "art",
          },
        ],
      },
      {
        code: "ARTEMIS-2",
        title: "Tighten player spawn collision checks around the lower ledges",
        assignee: {
          name: "Jamius Siam",
          avatarUrl: "/avatar.jpg",
        },
        status: "Todo",
        priority: "Medium",
        dateRange: "May 27 - May 29, 2026",
        tags: [
          {
            name: "dev",
          },
        ],
      },
    ],
  },
];

const Item = (): JSX.Element => {
  return (
    <div className="flex h-full min-h-0 flex-col gap-3">
      <Breadcrumb />
      <HorizontalRule />
      <Boards boards={boards} />
    </div>
  );
};

export const Route = createFileRoute("/dash/items")({
  component: Item,
});
