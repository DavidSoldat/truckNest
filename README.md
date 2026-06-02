# TruckNest 🚛

A multi-tenant SaaS fleet management platform built for small trucking companies in the ex-YU market. Manage trucks, drivers, clients, and invoices — all in one place.

> **Status:** In active development.

---

## Features

- 🏢 **Multi-tenancy** — row-level isolation per company, resolved from JWT — never from frontend
- 🚛 **Fleet management** — trucks, service records, tires
- 👥 **Driver management** — licenses, visa expiry tracking, salary records, incident logs
- 🧾 **Invoicing** — create, track, mark paid, send reminders, auto-mark overdue
- 📊 **Dashboard** — live stats, alert cards for upcoming services and expiring documents, full calendar view
- 🔔 **Notifications** — Kafka-based pipeline sends email alerts for service due, document expiry and overdue invoices
- 🔐 **Auth** — Keycloak OAuth2 with BFF pattern — browser never sees raw JWT tokens

---

## Architecture

TruckNest is a **modular monolith** built with Spring Modulith. Modules are strictly isolated — no module reaches into another module's internal package. Cross-module communication happens via public Query Services returning DTOs.

```
backend/
├── common/          # Shared entities, security, tenant context
├── trucks/          # Truck CRUD, service records, tires
├── drivers/         # Driver CRUD, km logs, salary, incidents
├── clients/         # Client CRUD, transport jobs
├── invoices/        # Invoice lifecycle, overdue scheduler
├── notifications/   # Kafka consumer, email via Brevo SMTP
├── dashboard/       # Redis-cached stats endpoint
├── companies/       # Company info endpoint
└── registration/    # Company + Keycloak user creation
```

**Multi-tenancy:** Every table has a `company_id` column. `TenantFilter` extracts `company_id` from the Keycloak JWT on every request and sets it in `TenantContext`. All repository queries are scoped to the current tenant.

**Frontend BFF pattern:** Next.js acts as Backend For Frontend. All Keycloak/token logic is server-side. Tokens are stored in iron-session encrypted httpOnly cookies — the browser never sees a raw JWT. All API calls go through `/api/proxy/*` which reads the session and forwards to Spring Boot with a Bearer token.

---

## Tech Stack

| Layer         | Technology                                    |
| ------------- | --------------------------------------------- |
| Backend       | Spring Boot 4, Java 21                        |
| Architecture  | Spring Modulith (modular monolith)            |
| Database      | PostgreSQL 16 + Flyway migrations             |
| Auth          | Keycloak 26 + Spring Security OAuth2          |
| Messaging     | Apache Kafka (KRaft)                          |
| Cache         | Redis 7 (dashboard, 60s TTL)                  |
| Email         | Brevo SMTP via JavaMailSender                 |
| Mapping       | MapStruct                                     |
| Observability | Prometheus + Grafana                          |
| Frontend      | Next.js 14 (App Router)                       |
| UI            | shadcn/ui + Tailwind CSS                      |
| State         | TanStack Query + Zustand                      |
| Deployment    | Oracle Cloud ARM (backend), Vercel (frontend) |

---
