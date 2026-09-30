import { createFileRoute } from "@tanstack/react-router";

import { BudgetPage } from "@/pages/BudgetPage";

export const Route = createFileRoute("/_authenticated/budget")({
  head: () => ({
    meta: [
      { title: "Budget — Travel.it" },
      { name: "description", content: "Set a travel budget and track every expense against it." },
    ],
  }),
  component: BudgetPage,
});
