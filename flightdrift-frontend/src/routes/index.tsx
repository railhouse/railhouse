import { createFileRoute } from "@tanstack/react-router";
import { Button } from "@/components/ui/button.tsx";

export const Route = createFileRoute("/")({
  component: Index,
});

function Index() {
  return (
    <div>
      <h3 className="text-4xl">Welcome Home!</h3>
      <p className="text-muted-text">Test Muted</p>
      <Button>Click Me</Button>
    </div>
  );
}
