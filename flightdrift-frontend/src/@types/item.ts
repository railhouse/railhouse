/*
 * Author: Jamius Siam
 * Since: 29/05/2026
 */
export type BoardItemTag = {
  name: string;
};

export type BoardItem = {
  code: string;
  title: string;
  assignee: {
    name: string;
    avatarUrl: string;
  };
  status: string;
  priority: string;
  dateRange: string;
  tags: BoardItemTag[];
};

export type Board = {
  title: string;
  items: BoardItem[];
};
