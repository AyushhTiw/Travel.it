# Travel.it - Backend

Spring Boot 3 REST API for the Travel.it platform. Handles authentication, trip management, budgets, AI chat, place discovery, and more.

---

## Stack

| Technology | Detail |
|-----------|--------|
| Java 21 | Language |
| Spring Boot 3 | Framework |
| Spring Security | JWT filter chain + OAuth2 |
| Spring Data JPA | ORM layer (Hibernate) |
| MySQL 8.4 | Database |
| Groq API | AI provider (qwen/qwen3.8-27b) |
| Google Places API | Nearby place discovery |
| Maven | Build tool |
| Docker | Containerisation |

---

## Getting started

    cd backend
    cp .env.example .env
    # fill in secrets (see env vars below)
    ./mvnw spring-boot:run

API starts on http://localhost:8080

For local database use the project root docker-compose.yml:

    docker compose up -d

---

## Package structure

Package-by-feature layout - all files for one domain live together:

    com.travelit
    +-- auth/         User entity, JWT, OAuth2, AuthController
    +-- trip/         Trip, TripDay, ItineraryItem, ItineraryVersion
    +-- budget/       Budget, BudgetItem
    +-- ai/           Trevvy chat, AiConversation, AiMessage, providers
    +-- explore/      Destination search and discovery
    +-- destination/  Destination, DestinationPlace
    +-- place/        Place entity, Google Places integration
    +-- review/       Place reviews
    +-- weather/      Weather data
    +-- category/     Category, PlaceCategory
    +-- interaction/  UserInteraction tracking
    +-- trust/        PlaceSource, PlaceVerification

---

## Authentication

Two flows:

**Email / password**
- Passwords hashed with BCrypt
- Returns JWT access token (15 min) + refresh token (7 days)
- JwtAuthFilter (OncePerRequestFilter) validates token on every request

**Google OAuth2**
- Spring Security OAuth2 client
- OAuth2SuccessHandler issues our own JWT after Google login
- Frontend always works with our tokens, never Google tokens

**Token refresh**

    POST /api/v1/auth/refresh
    Body: { refreshToken }
    Returns: new accessToken + refreshToken

---

## AI module

Provider pattern - swap models via AI_PROVIDER env var:

| Provider | Model |
|----------|-------|
| groq (default) | qwen/qwen3.8-27b |
| gemini | gemini-3.6-flash |
| mock | No API key needed (dev/test) |

Two chat modes:
- Guest chat (POST /api/v1/ai/chat) - stateless, no DB writes
- Authenticated chat (/api/v1/ai/conversations) - persisted history

Nearby place detection: regex matches 'near me', 'nearby', 'around here',
then calls Google Places API and injects real results into the AI prompt.

---

## API endpoints (summary)

| Module | Base path |
|--------|-----------|
| Auth | /api/v1/auth |
| Trips | /api/v1/trips |
| Budget | /api/v1/budget |
| AI chat | /api/v1/ai |
| Explore | /api/v1/explore |
| Destinations | /api/v1/destinations |
| Places | /api/v1/places |
| Reviews | /api/v1/reviews |
| Weather | /api/v1/weather |

See API_DOCUMENTATION.md at the project root for full endpoint details.

---

## Environment variables

| Variable | Description |
|----------|-------------|
| DB_HOST | MySQL host |
| DB_PORT | MySQL port (3306) |
| DB_NAME | Database name |
| DB_USERNAME | MySQL user |
| DB_PASSWORD | MySQL password |
| JWT_SECRET | 64-char signing secret |
| JWT_ACCESS_TOKEN_EXPIRATION | 900000 (15 min) |
| JWT_REFRESH_TOKEN_EXPIRATION | 604800000 (7 days) |
| AI_PROVIDER | groq / gemini / mock |
| GROQ_API_KEY | Groq key |
| GROQ_MODEL | qwen/qwen3.8-27b |
| GEMINI_API_KEY | Gemini key |
| GOOGLE_MAPS_API_KEY | Maps + Places |
| GOOGLE_CLIENT_ID | OAuth2 client ID |
| GOOGLE_CLIENT_SECRET | OAuth2 secret |
| FRONTEND_URL | CORS allowed origin |

---

## Build and deploy

    ./mvnw package -DskipTests
    java -jar target/travelit-backend.jar

Or with Docker (multi-stage build):

    docker build -t travelit-backend .[
    docker run -p 8080:8080 --env-file .env travelit-backend

See DOCKER.md for full container guide.
