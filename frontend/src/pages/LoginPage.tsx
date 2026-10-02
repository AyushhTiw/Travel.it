import { LoginForm } from "@/components/auth/LoginForm";
import { AuthShell } from "@/pages/AuthShell";

export function LoginPage() {
  return (
    <AuthShell
      title="Welcome Buddy"
      subtitle="Log in to pick up your plans where you left them."
    >
      <LoginForm />
    </AuthShell>
  );
}
