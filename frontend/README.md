# Travel.it — Your AI Travel Buddy

Frontend for Travel.it: discover places, plan trips and track budgets. It talks to an
existing Spring Boot backend and contains no backend code of its own.

## Getting started

```bash
npm install
cp .env.example .env
npm run dev      # local development
npm run build    # production build
```

## Environment variables

| Variable | Purpose |
| --- | --- |
| `VITE_API_BASE_URL` | Base URL of the Spring Boot API, e.g. `http://localhost:8080/api/v1` |

The backend URL is never hardcoded — every request goes through the single Axios
instance in `src/services/api.ts`, which reads `import.meta.env.VITE_API_BASE_URL`.
No secrets belong in this app; the frontend only ever knows the API base URL.

## Stack

React 19, TypeScript (strict), Vite, Tailwind CSS 4, TanStack Router (the router used by
this project; React Router is not available here), Axios, Lucide React.

## Folder structure

```
src/
  assets/        Logo asset
  components/
    auth/        LoginForm, SignupForm, AuthGuard, GuestGuard
    budget/      BudgetCard, BudgetForm, BudgetItem, BudgetSummary
    category/    CategoryChip, CategoryFilter
    common/      Button, Input, Modal, Loader, EmptyState, ErrorMessage, ConfirmDialog, Logo
    dashboard/   Welcome, QuickActions, UpcomingTrips, NearbyPreview, BudgetOverview
    destination/ DestinationCard, DestinationGrid, DestinationHeader
    explore/     ExploreSearch, NearbyPlaces, NearbyPlaceCard, LocationButton
    layout/      Navbar, Sidebar, Footer, AppLayout, MobileNavigation
    place/       PlaceCard, PlaceGrid, PlaceDetails
    trip/        TripCard, TripForm, TripHeader, TripTimeline, ItineraryItem
  context/       AuthContext
  hooks/         useAuth, useDestinations, useCategories, usePlaces, useExplore, useTrips, useBudget
  pages/         Screen-level components rendered by routes
  routes/        File-based routes (public + `_authenticated` protected subtree)
  services/      api (Axios instance), endpoints, and one service per backend module
  types/         Typed contracts for every backend response
  utils/         tokenStorage, formatDate, formatCurrency, validation
```

## API integration architecture

Strict one-way chain — components never call Axios directly:

```
Component → Hook → Service → Axios instance (src/services/api.ts) → Spring Boot
```

Endpoint paths live only in `src/services/endpoints.ts`, so a backend path change is a
single-file edit.

## Authentication flow

1. Sign up / log in returns `{ accessToken, refreshToken, userId, name, email, role }`.
2. Tokens are stored only through `src/utils/tokenStorage.ts` — nothing else touches
   `localStorage`.
3. The Axios request interceptor attaches `Authorization: Bearer <token>` to every
   request except `/auth/login`, `/auth/signup` and `/auth/refresh`.
4. On a `401`, the response interceptor performs a single-flight refresh, retries the
   original request once, and on failure clears the session and sends the user to
   `/login`. There is no retry loop.
5. On startup `AuthContext` restores the session via `GET /auth/me`, falling back to a
   refresh attempt, and protected pages stay hidden while that check runs.
6. Logout calls the backend, clears both tokens and the user state, then redirects.

## Routing

Public: `/`, `/login`, `/signup`. Protected (inside the `_authenticated` layout):
`/dashboard`, `/explore`, `/destinations`, `/destinations/:destinationId`, `/places`,
`/places/:placeId`, `/trips`, `/trips/create`, `/trips/:tripId`, `/budget`, `/profile`.
Signed-in visitors on `/login` or `/signup` are redirected to `/dashboard`; unknown URLs
render the themed 404 page.

## Backend modules

Implemented and wired: Auth, Destination, Category, Place, Explore, Budget.

**Trip is still being implemented on the backend.** `src/services/tripService.ts` holds
the typed abstraction and a `TRIP_BACKEND_READY` flag set to `false`; it rejects with
`TripIntegrationPendingError` instead of calling invented endpoints or faking responses.
Trip screens show a clear "integration pending" state. Flip the flag once the endpoints
are live — no other file needs to change.

## Notes

- Geolocation is requested only when the user presses "Use my location".
- Every API-driven screen has loading, empty, error and success states.
- No user data is hardcoded anywhere; all of it comes from the authenticated account.
