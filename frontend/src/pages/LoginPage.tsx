import { Link } from "@tanstack/react-router";

import { LoginForm } from "@/components/auth/LoginForm";
import { AuthShell } from "@/pages/AuthShell";

export function LoginPage() {
  return (
    <AuthShell
      title="Welcome back"
      subtitle="Log in to pick up your plans where you left them."
      footer={
        <p className="text-sm text-muted-foreground">
          New to Travel.it?{" "}
          <Link to="/signup" className="font-medium text-primary hover:underline">
            Create an account
          </Link>
        </p>
      }
    >
      <LoginForm />
    </AuthShell>
  );
}
