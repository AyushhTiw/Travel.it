# Travel.it Backend

> Spring Boot REST API backend for Travel.it platform.

## Technology Stack

- Spring Boot 3.5.16, Java 25
- MySQL 8.0, Spring Data JPA
- Spring Security + JWT
- Google OAuth 2.0
- Groq/Gemini AI Integration
- Maven Build Tool

## Installation
```bash
cd backend
mvn clean install
```

## Environment Setup

Create .env file (see .env.example):
```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=travelit
DB_USERNAME=root
DB_PASSWORD=your_password

JWT_SECRET=your_secret_key_min_256_bits

GOOGLE_CLIENT_ID=your_google_oauth_id
GOOGLE_CLIENT_SECRET=your_google_oauth_secret

AI_PROVIDER=groq
GROQ_API_KEY=your_groq_key

GOOGLE_MAPS_API_KEY=your_maps_key

FRONTEND_URL=http://localhost:5173
```

## Running
```bash
mvn spring-boot:run
```

Server: http://localhost:8080

## Database Setup
```sql
CREATE DATABASE travelit CHARACTER SET utf8mb4;
```

## Project Structure
```text
backend/src/main/java/com/travelit/
├── auth/         # Authentication & OAuth
├── trip/         # Trip management
├── budget/       # Budget tracking
├── ai/           # AI integration (Trevvy)
├── explore/      # Places exploration
├── destination/  # Destinations
└── config/       # Security config
```

## Security

- JWT authentication
- Google OAuth 2.0
- CORS configured
- BCrypt password encryption

## API Documentation

See API_DOCUMENTATION.md for complete API reference.

## Deployment

Render/Railway:
- Build: mvn clean package -DskipTests
- Run: java -jar target/travelit-backend-0.0.1-SNAPSHOT.jar

## License

Copyright 2024 Travel.it
