import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return (
    <div>
      <h3 className="text-4xl">Welcome Home!</h3>
    </div>
  );
}
