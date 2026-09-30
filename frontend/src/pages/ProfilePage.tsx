import { LogOut, Mail, RefreshCw, User as UserIcon } from "lucide-react";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/common/Button";
import { Loader } from "@/components/common/Loader";
import { AppLayout } from "@/components/layout/AppLayout";
import { useAuth } from "@/hooks/useAuth";

export function ProfilePage() {
  const { user, logout, refreshUser } = useAuth();

  if (!user) return <Loader label="Loading your profile" />;

  const rows = [
    { icon: UserIcon, label: "Name", value: user.name },
    { icon: Mail, label: "Email", value: user.email },
  ];

  return (
    <AppLayout title="Profile" description="Your Travel.it account details.">
      <div className="max-w-2xl space-y-6">
        <section className="rounded-3xl border border-border bg-card p-6 sm:p-8">
          <div className="flex items-center gap-4">
            <Avatar className="size-20">
              {user.profilePicture && <AvatarImage src={user.profilePicture} alt={user.name} />}
              <AvatarFallback className="bg-primary text-2xl font-semibold text-primary-foreground">
                {user.name.charAt(0).toUpperCase()}
              </AvatarFallback>
            </Avatar>
            <div className="min-w-0 flex-1">
              <p className="truncate text-lg font-semibold text-foreground">{user.name}</p>
            </div>
          </div>

          <dl className="mt-8 space-y-3">
            {rows.map((row) => (
              <div
                key={row.label}
                className="grid grid-cols-[minmax(0,1fr)_minmax(0,1.4fr)] items-center gap-4 rounded-2xl bg-secondary px-4 py-3.5"
              >
                <dt className="flex min-w-0 items-center gap-2 text-sm text-muted-foreground">
                  <row.icon className="size-4 shrink-0 text-primary" aria-hidden />
                  <span className="truncate">{row.label}</span>
                </dt>
                <dd className="truncate text-sm font-medium text-foreground">{row.value}</dd>
              </div>
            ))}
          </dl>

          <div className="mt-8 flex flex-wrap gap-3">
            <Button
              variant="outline"
              onClick={() => void refreshUser()}
              leftIcon={<RefreshCw className="size-4" aria-hidden />}
            >
              Refresh details
            </Button>
            <Button variant="danger" onClick={() => void logout()} leftIcon={<LogOut className="size-4" aria-hidden />}>
              Log out
            </Button>
          </div>
        </section>
      </div>
    </AppLayout>
  );
}
