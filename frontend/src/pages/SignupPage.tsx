import { Link } from "@tanstack/react-router";

import { SignupForm } from "@/components/auth/SignupForm";
import { AuthShell } from "@/pages/AuthShell";

export function SignupPage() {
  return (
    <AuthShell
      title="Create your account"
      subtitle="Start planning trips that actually feel like yours."
      footer={
        <p className="text-sm text-muted-foreground">
          Already have an account?{" "}
          <Link to="/login" className="font-medium text-primary hover:underline">
            Log in
          </Link>
        </p>
      }
    >
      <SignupForm />
    </AuthShell>
  );
}
