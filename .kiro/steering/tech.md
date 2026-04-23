# Tech Stack

## Backend

- **Language**: Java 24
- **Framework**: Spring Boot 3.5.x with `@SpringBootApplication`
- **Event Sourcing**: FluxZero SDK (`io.fluxzero:sdk`) — commands, queries, aggregates, projections, event store
- **Modularity**: Spring Modulith for enforcing module boundaries
- **Build**: Maven (multi-module), wrapper included (`./mvnw`)
- **Annotation Processing**: Lombok + FluxZero SDK
- **Auth**: Auth0 java-jwt + jwks-rsa, Zitadel as identity provider
- **Payments**: PayPal Server SDK
- **Geospatial**: JTS Geometry via `jackson-datatype-jts`
- **JSON Patching**: zjsonpatch (WebSocket UI updates)
- **Validation**: Jakarta Bean Validation (`jakarta.validation`)

## Frontend

- **Framework**: Angular 21 (standalone components)
- **UI Library**: Angular Material + Bootstrap 5
- **Language**: TypeScript 5.9, strict templates enabled
- **Auth**: `@edgeflare/ngx-oidc` (OpenID Connect)
- **Component prefix**: `track-rejoice`
- **Shared models**: Auto-generated TypeScript from Java via `typescript-generator-maven-plugin`, consumed as a local npm package (`@trackrejoice/typescriptmodels`)

## Lombok Configuration

```
lombok.anyConstructor.addConstructorProperties=true
lombok.equalsandhashcode.callsuper=skip
```

## Local Development Infrastructure

- Docker Compose in `local/` — FluxZero test server, proxy, OpenSearch, dashboards, audit log
- FluxZero Test Server: port 8888
- FluxZero Proxy: port 8080
- `TestApp.java` boots the full stack (test server + proxy + Spring app) in-process for development

## Common Commands

```bash
# Full build
./mvnw clean install

# Run tests
./mvnw test

# Run tests for a specific module
./mvnw test -pl sighting

# Build without tests
./mvnw clean install -DskipTests

# Run the app (starts test server + proxy + Spring Boot)
# Use IntelliJ run configs or:
./mvnw exec:java -pl app

# Package TypeScript models (runs during Maven package phase)
./mvnw package -pl app

# Start local infrastructure
docker compose -f local/docker-compose.yml up

# Frontend (from ui/ directory)
cd ui && npm install && npm start
```
