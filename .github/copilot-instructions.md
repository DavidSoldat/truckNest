# TruckNest AI Coding Guidelines

## Architecture Overview
TruckNest is a multi-tenant truck management system built as a modular monolith using Spring Modulith. It features event-driven communication via Kafka, with modules for trucks, drivers, invoices, notifications, and dashboard. Tenancy is enforced at the database level using a `company_id` UUID column on all entities.

## Multi-Tenancy Implementation
- **Tenant Identification**: Extract `company_id` from JWT claims in `TenantFilter` and store in `ThreadLocal` via `TenantContext`.
- **Entity Base Class**: All domain entities extend `BaseEntity`, which includes `companyId` (UUID), `createdAt`, `updatedAt`, and `createdBy` (from JWT `sub`).
- **Database Schema**: Use Flyway migrations in `backend/src/main/resources/db/migration/`. Ensure all tables have `company_id` column for row-level security.
- **Example**: When creating a new truck entity, set `companyId` from `TenantContext.getTenantId()` implicitly via JPA listeners.

## Security & Authentication
- **OAuth2 Resource Server**: Secured with Keycloak JWT tokens. Public endpoints: `/actuator/health`, `/actuator/info`, `/actuator/prometheus`.
- **Tenant Enforcement**: `TenantFilter` runs before security, rejecting requests without `company_id` claim (except actuator).
- **Auditing**: `AuditorAwareImpl` provides current user from JWT `sub` for `@CreatedBy`.

## Development Workflow
- **Local Setup**: Run `docker-compose up postgres redis keycloak kafka` for dependencies, then `mvn spring-boot:run` with `--spring.profiles.active=local`.
- **Full Stack**: `docker-compose --profile full up` builds and runs backend (port 8081), frontend (port 3000), and infrastructure.
- **Build**: `mvn clean install` for backend. Use `./mvnw` wrapper.
- **Testing**: Unit tests with Spring Boot Test, integration tests with Testcontainers (implied by dependencies).
- **Migrations**: Add SQL files in `db/migration/` following `V{number}__{description}.sql` pattern. Run `mvn flyway:migrate` or via app startup.

## Code Patterns
- **Modulith Events**: Publish domain events using `@DomainEvent` and handle with `@EventListener` across modules (e.g., truck created → notification).
- **Kafka Integration**: Use `KafkaTemplate` for async messaging, consumer group `trucknest-notifications`.
- **Redis**: Cache with `RedisTemplate` for performance.
- **Validation**: Use Bean Validation on DTOs and entities.
- **Lombok**: Prefer `@Getter/@Setter` on entities, avoid on DTOs for explicit control.

## Key Files
- `docker-compose.yml`: Service orchestration with profiles (`full` for complete app).
- `backend/pom.xml`: Spring Boot 4.0.6, Java 21, dependencies include Modulith, Kafka, Redis.
- `backend/src/main/resources/application.yaml`: Config defaults; override in `application-local.yaml`.
- `infrastructure/keycloak/realm-export.json`: Keycloak realm config for auth.
- `backend/src/main/java/com/trucknest/backend/common/tenant/TenantFilter.java`: Tenancy filter example.

## Conventions
- **Package Structure**: `com.trucknest.backend.{module}` for features, `common` for shared (entity, security, tenant).
- **Naming**: Snake_case for DB columns (e.g., `company_id`), camelCase for Java fields.
- **Error Handling**: Use Spring's default; custom exceptions for domain logic.
- **Logging**: Use SLF4J via Spring Boot logging.

Focus on event-driven architecture for loose coupling between modules. Always validate tenancy in queries and mutations.</content>
<parameter name="filePath">c:\Users\david\Desktop\truckNest\.github\copilot-instructions.md