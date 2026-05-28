import { createFileRoute } from "@tanstack/react-router";
import Header from "@/components/header/header.tsx";
import Sidebar from "@/components/header/sidebar.tsx";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return (
    <div className="flex flex-col gap-3.5 p-4 w-full h-screen">
      <Header />
      <div className="flex-1 flex gap-3">
        <Sidebar />
        <main className="bg-white flex-1 drop-shadow-md drop-shadow-black/2 rounded-lg p-4">
          Lorem ipsum dolor sit amet, consectetur adipisicing elit. Commodi distinctio dolorum enim
          error explicabo illum nam nulla odit quisquam quod.
        </main>
      </div>
    </div>
  );
}
