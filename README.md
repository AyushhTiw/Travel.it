# Travel.it - Your AI Travel Buddy

Full-stack travel planning platform. React + TypeScript frontend, Spring Boot backend.

**Live app:** https://travelit-tan.vercel.app

---

## Features

- Explore destinations and discover nearby places via Google Maps
- Build day-by-day trip itineraries with notes and timings
- Track budgets with category breakdown
- Chat with Trevvy - AI travel assistant powered by Groq (Qwen 27B)
- Sign in with email/password or Google OAuth2

---

## Repo layout

    frontend/      React + TypeScript + Vite (Vercel)
    backend/       Spring Boot 3 REST API (Java 21, Docker)
    docs/          Documentation
    docker-compose.yml

---

## Tech stack

| Layer | Technology |
|-------|------------|
| Frontend | React 19, TypeScript, Vite, Tailwind CSS, shadcn/ui |
| Routing | TanStack Router |
| State management | TanStack Query |
| Backend | Spring Boot 3, Java 21 |
| Database | MySQL 8.4 |
| ORM | Spring Data JPA + Hibernate |
| Auth | JWT (HS256) + Google OAuth2 |
| AI | Groq API - qwen/qwen3.8-27b (Gemini fallback) |
| Maps | Google Maps + Places API |
| Deploy | Vercel (frontend), Docker (backend) |

---

## Local setup

### 1. Start the database

    docker compose up -d

### 2. Run the backend

    cd backend
    cp .env.example .env
    ./mvnw spring-boot:run

API runs on http://localhost:8080

### 3. Run the frontend

    cd frontend
    npm install
    npm run dev

UI runs on http://localhost:5173

---

## Environment variables

| Variable | Description |
|----------|-------------|
| DB_HOST / DB_PORT / DB_NAME | MySQL connection |
| JWT_SECRET | 64-char signing secret |
| AI_PROVIDER | groq / gemini / mock |
| GROQ_API_KEY | Groq API key |
| GOOGLE_MAPS_API_KEY | Maps + Places |
| GOOGLE_CLIENT_ID / GOOGLE_CLIENT_SECRET | OAuth2 |
| FRONTEND_URL | CORS allowed origin |

---

## Documentation

| File | Contents |
|------|----------|
| frontend/README.md | Frontend stack, structure, auth flow, routing |
| backend/README.md | Backend modules, security, API endpoints |
| API_DOCUMENTATION.md | All REST endpoints with payloads |
| DOCKER.md | Docker and Compose usage guide |
