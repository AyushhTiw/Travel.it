import { useState, type FormEvent } from "react";
import { Link, useNavigate } from "@tanstack/react-router";
import { Lock, Mail } from "lucide-react";

import { Button } from "@/components/common/Button";
import { ErrorMessage } from "@/components/common/ErrorMessage";
import { Input } from "@/components/common/Input";
import { useAuth } from "@/hooks/useAuth";
import { toFriendlyMessage } from "@/services/api";
import {
  hasErrors,
  validateLogin,
  type LoginFormValues,
  type ValidationErrors,
} from "@/utils/validation";

export function LoginForm() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState<ValidationErrors<LoginFormValues>>({});
  const [formError, setFormError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setFormError(null);
    const validation = validateLogin({ email, password });
    setErrors(validation);
    if (hasErrors(validation)) return;

    setIsSubmitting(true);
    try {
      await login({ email: email.trim(), password });
      void navigate({ to: "/dashboard", replace: true });
    } catch (error: unknown) {
      setFormError(toFriendlyMessage(error, "We couldn't sign you in. Check your details and try again."));
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleGoogleLogin = () => {
    window.location.href =
      "http://localhost:8080/oauth2/authorization/google";
  };

  return (
    <form onSubmit={handleSubmit} noValidate className="space-y-5">
      {formError ? <ErrorMessage compact message={formError} /> : null}

      <Input
        label="Email"
        type="email"
        autoComplete="email"
        placeholder="you@example.com"
        icon={<Mail className="size-4" aria-hidden />}
        value={email}
        onChange={(event) => setEmail(event.target.value)}
        error={errors.email}
        required
      />

      <Input
        label="Password"
        type="password"
        autoComplete="current-password"
        placeholder="••••••••"
        icon={<Lock className="size-4" aria-hidden />}
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        error={errors.password}
        required
      />

      <Button
        type="submit"
        size="lg"
        className="w-full"
        isLoading={isSubmitting}
      >
        Log in
      </Button>

      <div className="relative flex items-center">
        <div className="flex-1 border-t border-border" />
        <span className="px-3 text-xs text-muted-foreground">OR</span>
        <div className="flex-1 border-t border-border" />
      </div>

      <button
        type="button"
        onClick={handleGoogleLogin}
        className="inline-flex w-full items-center justify-center gap-2 rounded-full border border-border bg-card px-5 py-3 text-sm font-medium text-foreground transition-colors hover:bg-secondary"
      >
        <svg className="size-5" viewBox="0 0 24 24" aria-hidden>
          <path
            d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
            fill="#4285F4"
          />
          <path
            d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
            fill="#34A853"
          />
          <path
            d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
            fill="#FBBC05"
          />
          <path
            d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
            fill="#EA4335"
          />
        </svg>
        Continue with Google
      </button>

      <p className="text-center text-sm text-muted-foreground">
        New to Travel.it?{" "}
        <Link
          to="/signup"
          className="font-medium text-primary hover:underline"
        >
          Create an account
        </Link>
      </p>
    </form>
  );
}