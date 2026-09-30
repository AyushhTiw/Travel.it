import { AppLayout } from "@/components/layout/AppLayout";
import { BudgetOverview } from "@/components/dashboard/BudgetOverview";
import { NearbyPreview } from "@/components/dashboard/NearbyPreview";
import { QuickActions } from "@/components/dashboard/QuickActions";
import { UpcomingTrips } from "@/components/dashboard/UpcomingTrips";
import { WelcomeSection } from "@/components/dashboard/WelcomeSection";
import { DestinationGrid } from "@/components/destination/DestinationGrid";
import { Loader } from "@/components/common/Loader";
import { useAuth } from "@/hooks/useAuth";
import { useBudget } from "@/hooks/useBudget";
import { useDestinations } from "@/hooks/useDestinations";
import { useDashboardPlaces } from "@/hooks/useDashboardPlaces";
import { useExplore } from "@/hooks/useExplore";
import { useTrips } from "@/hooks/useTrips";

export function DashboardPage() {
  const { user } = useAuth();
  const trips = useTrips();
  const budget = useBudget();
  const destinations = useDestinations();
  
  // Get user''s current location from explore context (but don''t auto-request)
  const explore = useExplore();
  
  // Fetch nearby places using Google Places API (not database places)
  const dashboardPlaces = useDashboardPlaces(
    explore.coordinates?.latitude ?? null,
    explore.coordinates?.longitude ?? null
  );

  if (!user) return <Loader label="Loading your account" />;

  return (
    <AppLayout>
      <div className="space-y-12">
        <WelcomeSection user={user} />
        <QuickActions />
        <UpcomingTrips
          trips={trips.trips}
          isLoading={trips.isLoading}
          error={trips.error}
          isPendingIntegration={trips.isPendingIntegration}
        />
        <NearbyPreview 
          places={dashboardPlaces.places} 
          isLoading={dashboardPlaces.isLoading} 
          error={dashboardPlaces.error}
          onRetry={dashboardPlaces.retry}
        />
        <BudgetOverview budgets={budget.budgets} isLoading={budget.isLoading} error={budget.error} />

        <section aria-labelledby="recommended-heading">
          <h2
            id="recommended-heading"
            className="mb-4 text-sm font-semibold uppercase tracking-wide text-muted-foreground"
          >
            Recommended destinations
          </h2>
          <DestinationGrid
            destinations={destinations.destinations.slice(0, 3)}
            isLoading={destinations.isLoading}
            error={destinations.error}
            onRetry={destinations.reload}
          />
        </section>
      </div>
    </AppLayout>
  );
}
