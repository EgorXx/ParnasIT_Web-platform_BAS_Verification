FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app
COPY ../gradlew settings.gradle.kts build.gradle.kts ./
COPY ../gradle gradle
COPY backend backend
RUN ./gradlew :backend-api:bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/backend/backend-api/build/libs/backend-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
