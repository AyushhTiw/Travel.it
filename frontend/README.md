# Travel.it - Frontend

React + TypeScript frontend for the Travel.it travel planning platform.
Communicates with the Spring Boot backend via REST API.

**Live:** https://travelit-tan.vercel.app

---

## Stack

| Technology | Detail |
|-----------|--------|
| React 19 | UI library |
| TypeScript | Strict mode |
| Vite | Build tool (ESBuild, fast HMR) |
| Tailwind CSS | Utility-first styling |
| shadcn/ui | Accessible component library (Radix UI) |
| TanStack Router | File-based routing |
| TanStack Query | Server state, caching, background refetch |
| Axios | HTTP client |
| Lucide React | Icon library |

---

## Getting started

    cd frontend
    npm install
    cp .env.example .env
    npm run dev

UI runs on http://localhost:5173

### Environment variable

| Variable | Value |
|----------|-------|
| VITE_API_BASE_URL | http://localhost:8080/api/v1 |

The base URL is the only env var. All requests go through the single
Axios instance in src/services/api.ts - nothing else touches it.

---

## Folder structure

    src/
    +-- assets/          Logo
    +-- components/
    |   +-- auth/         LoginForm, SignupForm, AuthGuard, GuestGuard
    |   +-- budget/       BudgetCard, BudgetForm, BudgetItem, BudgetSummary
    |   +-- category/     CategoryChip, CategoryFilter
    |   +-- common/       Button, Input, Modal, Loader, EmptyState, Logo
    |   +-- dashboard/    Welcome, QuickActions, NearbyPreview, BudgetOverview
    |   +-- destination/  DestinationCard, DestinationGrid
    |   +-- explore/      ExploreSearch, NearbyPlaces, LocationButton
    |   +-- layout/       Navbar, Sidebar, Footer, AppLayout
    |   +-- place/        PlaceCard, PlaceGrid, PlaceDetails
    |   +-- trip/         TripCard, TripForm, TripTimeline, ItineraryItem
    |   +-- ai/           Trevvy chat component
    +-- hooks/           useAuth, useTrips, useBudget, useExplore, ..
    +-- pages/           Screen-level components (route targets)
    +-- routes/          File-based route definitions
    +-- services/        Axios instance + one service per backend module
    +-- types/           TypeScript contracts for every API response
    +-- utils/           tokenStorage, formatDate, formatCurrency

---

## Routing

Public routes: / (landing), /login, /signup

Protected routes (inside _authenticated layout):
/dashboard, /explore, /destinations, /destinations/:id,
/places, /places/:id, /trips, /trips/create, /trips/:id,
/budget, /profile

Signed-in users on /login or /signup are redirected to /dashboard.
Unknown URLs render the 404 page.

---

## Authentication flow

1. Login returns accessToken + refreshToken
2. Tokens stored only via src/utils/tokenStorage.ts
3. Axios interceptor attaches Authorization: Bearer header to every request
4. On 401 - interceptor calls /auth/refresh once, retries original request
5. On refresh failure - clears session, redirects to /login
6. On startup - AuthContext restores session via GET /auth/me

---

## API call pattern

Strict one-way chain:

    Component -> Hook -> Service -> Axios (src/services/api.ts) -> Backend

Endpoint paths live only in src/services/endpoints.ts.
A backend path change is a single-file edit.

---

## Build

    npm run build

Outputs to dist/. Deploy dist/ to any static host (Vercel auto-deploys from main).

---

## Deployment

Vercel. Auto-deploys on push to main.
Set VITE_API_BASE_URL in Vercel environment settings.
