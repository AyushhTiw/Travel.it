# Travel.it

> AI-powered travel companion platform for smart trip planning and exploration.

## Overview

Travel.it helps users plan, organize, and discover travel experiences with AI assistance. Built with React frontend and Spring Boot backend.

## Architecture
```text
Travel.it/
├── frontend/    # React + TypeScript + Vite
├── backend/     # Spring Boot + MySQL
└── docs/        # Documentation
```

### Tech Stack

**Frontend:** React 19, TypeScript, TanStack Router/Query, TailwindCSS
**Backend:** Spring Boot 3.5, Java 25, MySQL, Spring Security
**Deployment:** Frontend (Vercel), Backend (Render)

## Quick Start

### Prerequisites
- Node.js 18+, Java 25, MySQL 8.0, Maven 3.9+

### Setup Backend
```bash
cd backend
cp .env.example .env
# Edit .env with your credentials
mvn clean install
mvn spring-boot:run
```

Backend: http://localhost:8080

### Setup Frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend: http://localhost:5173

## Features

### Trip Planning
- Create and manage trips
- AI-powered itinerary generation (Trevvy)
- Day-by-day planning
- Collaborative sharing

### Budget Management
- Per-trip budget tracking
- Category-wise allocation
- Visual breakdown
- Real-time calculations

### Explore & Discover
- Google Maps integration
- Search nearby places
- Location-based recommendations
- Ratings and reviews

### AI Assistant (Trevvy)
- Natural language trip planning
- Context-aware recommendations
- Smart itinerary suggestions

### Authentication
- Email/password login
- Google OAuth 2.0
- JWT-based sessions

### Dashboard
- Time-based personalized greetings
- Quick action shortcuts
- Upcoming trips overview
- Budget summary
- Nearby places preview

## Documentation

- Frontend README: frontend/README.md
- Backend README: backend/README.md
- API Documentation: API_DOCUMENTATION.md

## Deployment

**Frontend (Vercel):** Auto-deploy from main branch
**Backend (Render):** Auto-deploy from main branch

## Contributing

1. Fork repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Open Pull Request

## License

Copyright 2024 Travel.it. All rights reserved.

## Links

- Repository: https://github.com/AyushhTiw/Travel.it
- Live Demo: https://travel-it-frontend.vercel.app
- API: https://travel-it-backend.onrender.com
