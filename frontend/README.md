# Travel.it Frontend

> Modern React + TypeScript frontend for Travel.it AI-powered travel platform.

## Technology Stack

- React 19.2.0, Vite 5.0.0, TypeScript 5.8.3
- TanStack Router 1.170.18, TanStack Query 5.101.1
- TailwindCSS 4.2.1, Radix UI Components
- Google Maps Integration, Axios 1.20.0

## Installation
```bash
cd frontend
npm install
```

## Environment Setup

Create .env file:
```env
VITE_API_BASE_URL=http://localhost:8080
VITE_GOOGLE_MAPS_API_KEY=your_key
```

## Commands
```bash
npm run dev      # Dev server (localhost:5173)
npm run build    # Production build
npm run preview  # Preview build
npm run lint     # ESLint
npm run format   # Prettier
```

## Features

- Dashboard with time-based greetings
- Trip planning with AI itinerary generation
- Budget tracking and management
- Google Maps exploration
- Review and rating system
- Google OAuth authentication

## Deployment

Vercel configuration:
- Build: npm run build
- Output: dist/

## License

Copyright 2024 Travel.it
