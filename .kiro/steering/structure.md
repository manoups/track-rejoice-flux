# Project Structure

Maven multi-module monorepo. Group ID: `com.breece`, base package: `com.breece.*`.

## Modules

| Module | Purpose |
|---|---|
| `core-api` | Shared domain types: authentication, user model, faceted search, geometry utils. Depended on by all domain modules. |
| `sighting` | Sighting aggregate — commands, queries, API models |
| `content` | Content aggregate + Proposal/WeightedAssociation aggregate — commands, queries, API models |
| `order` | Order aggregate + payment handling — commands, order fulfillment, payment process |
| `service` | Service aggregate — purchasable services for content |
| `app` | Spring Boot application — REST/WebSocket endpoints, wires all modules together. Component scan: `com.breece` |
| `local-auth` | Local authentication stub (activated via Maven `local` profile) |
| `local-seed` | Seed data loader for local development |
| `ui` | Angular 21 frontend application |
| `local` | Docker Compose infrastructure for local dev |

## Module Dependency Flow

```
core-api  ←  sighting  ←  content  ←  order
core-api  ←  service   ←  order
core-api  ←  all domain modules
app       ←  order, service, sighting, content (aggregates everything)
```

## Domain Module Internal Layout

Each domain module follows this package convention under `com.breece.{module}`:

```
api/
  model/          # Domain model records (aggregates, value objects, IDs)
  {Module}Errors  # Error definitions
command/
  api/            # Command records, command handler interface, DTOs
query/
  api/            # Query records, projection/document records
```

## Key Patterns

- **Aggregates**: Java `record` types annotated with `@Aggregate(searchable = true)`, using `@EntityId` for identity and `@Builder(toBuilder = true)` via Lombok.
- **Commands**: Java `record` types implementing a `{Module}Command` interface. The interface provides a default `@HandleCommand` method that loads the aggregate and applies the command.
- **Queries**: Java `record` types used with `Fluxzero.queryAndWait(...)`.
- **Projections**: `{Module}Document` records that wrap `SearchHit<Aggregate>` for read-optimized views.
- **IDs**: Dedicated ID record types per aggregate (e.g., `SightingId`, `ContentId`, `UserId`).
- **Endpoints**: `@Component` classes in `app/web/` using FluxZero web annotations (`@Path`, `@HandleGet`, `@HandlePost`, `@HandleDelete`, `@PathParam`). Endpoints delegate to `Fluxzero.sendCommandAndWait(...)` and `Fluxzero.queryAndWait(...)`.
- **Routing**: Commands use `@RoutingKey` on the aggregate ID field.
- **Validation**: Jakarta `@NotNull` on command/model fields.
- **Ownership**: Domain models implement `WithOwner` interface for user-scoped access.
