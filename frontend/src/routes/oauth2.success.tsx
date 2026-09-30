import { createFileRoute, useNavigate } from '@tanstack/react-router';
import { useEffect } from 'react';
import { Loader } from '@/components/common/Loader';
import { setTokens } from '@/utils/tokenStorage';
import { useAuth } from '@/hooks/useAuth';

export const Route = createFileRoute('/oauth2/success')({
  component: OAuth2SuccessPage,
});

function OAuth2SuccessPage() {
  const navigate = useNavigate();
  const { refreshUser } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');
    const refreshToken = params.get('refreshToken');

    if (accessToken && refreshToken) {
      // Store tokens in localStorage
      setTokens(accessToken, refreshToken);

      // Refresh user data in AuthContext
      refreshUser()
        .then(() => {
          // Redirect to dashboard
          void navigate({ to: '/dashboard', replace: true });
        })
        .catch((error) => {
          console.error('Failed to fetch user after OAuth2:', error);
          // Redirect to login on error
          void navigate({ to: '/login', replace: true });
        });
    } else {
      // No tokens found, redirect to login
      void navigate({ to: '/login', replace: true });
    }
  }, [navigate, refreshUser]);

  return (
    <div className="flex min-h-screen items-center justify-center bg-background">
      <Loader label="Signing you in..." />
    </div>
  );
}
