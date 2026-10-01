# =========================
# BUILD STAGE
# =========================
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests


# =========================
# RUN STAGE
# =========================
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render provides PORT environment variable
# Expose it for documentation (actual port comes from $PORT at runtime)
EXPOSE ${PORT:-10000}

# Use shell form to allow environment variable expansion
ENTRYPOINT java -Dserver.port=${PORT:-10000} -jar app.jar
