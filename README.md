# BAS Verification

Веб-сервис подачи и согласования планов полётов беспилотных авиационных систем (БАС).

Автоматическая проверка маршрутов на соответствие геозонам (PostGIS). Спорные заявки направляются на ручное рассмотрение администратору.

## Стек

- **Бэкенд:** Java 25, Spring Boot 4.x, Gradle (multi-module)
- **База данных:** PostgreSQL 16 + PostGIS 3.4
- **Кэш / сессии:** Redis 7
- **Фронтенд:** React 19, Vite 6, TypeScript 5.7, Leaflet

## Быстрый старт

```bash
# Поднять инфраструктуру
docker compose -f docker/docker-compose.yml up -d

# Собрать и запустить бэкенд
./gradlew :backend-api:bootRun

# Фронтенд (опционально)
cd frontend && npm install && npm run dev
```

API-документация (Swagger): http://localhost:8080/swagger-ui.html

## Структура проекта

```
backend/
├── backend-api/          # REST-контроллеры, DTO, точка входа
├── backend-core/         # Бизнес-логика, алгоритм автопроверки
├── backend-security/     # JWT, Spring Security
└── backend-persistence/  # JPA-сущности, PostGIS, Flyway-миграции

frontend/                 # React SPA
docker/                   # docker-compose (PostgreSQL + PostGIS + Redis)
```
