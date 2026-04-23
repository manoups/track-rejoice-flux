# Implementation Plan: Pet Content SSE Dashboard

## Overview

Build a three-panel Angular dashboard at `/contents/:id` for pet content items, backed by a new SSE endpoint for real-time `WeightedAssociationState` updates and a REST endpoint for sighting history. The backend uses Spring Boot `SseEmitter` with FluxZero `@HandleNotification` for event fan-out. The frontend introduces an `SseService` wrapping `EventSource` with exponential backoff reconnection, and four new standalone components (dashboard shell, content details panel, sighting history map, proposals table).

## Tasks

- [x] 1. Create backend SSE infrastructure
  - [x] 1.1 Create `SseEventData` record with `ChangeType` enum
    - Create `app/src/main/java/com/breece/app/web/api/SseEventData.java`
    - Define `record SseEventData(ChangeType changeType, Object payload)` with inner `enum ChangeType { INITIAL, CREATED, UPDATED, DELETED }`
    - _Requirements: 2.1_

  - [x] 1.2 Implement `ContentSseEndpoint` with SSE stream and notification handling
    - Create `app/src/main/java/com/breece/app/web/ContentSseEndpoint.java`
    - Annotate with `@Component` and `@Path("/api/content")`
    - Implement `@HandleGet("{contentId}/proposals/stream")` that creates an `SseEmitter` (timeout 0), authenticates the user via `Sender`, validates content ownership (403 if unauthorized, 404 if not found), sends initial snapshot of `WeightedAssociationState` records with `changeType: INITIAL` and `event: snapshot`, and registers the emitter in the active connections registry
    - Implement `@HandleNotification` on `Entity<WeightedAssociationState>` that filters by `contentId`, determines `changeType` (CREATED/UPDATED/DELETED based on entity state), serializes as `SseEventData`, and writes to all registered emitters for that content with `event: update`
    - Use `ConcurrentHashMap<String, Set<SseEmitter>>` for active connections keyed by contentId string
    - Use `ConcurrentHashMap<SseEmitter, AtomicLong>` for per-emitter monotonic event IDs
    - Register `onCompletion`, `onTimeout`, and `onError` callbacks on each `SseEmitter` to remove it from both maps
    - For DELETED notifications, include only `weightedAssociationId` in the payload
    - Follow the existing `UiUpdateSocketEndpoint` pattern for auth and notification handling
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 2.1, 2.2, 2.3, 8.1, 8.2, 8.3, 8.4, 8.5_

  - [x] 1.3 Add sighting history endpoint to `ContentEndpoint`
    - Add `@HandleGet("{contentId}/sighting-history")` method to `app/src/main/java/com/breece/app/web/ContentEndpoint.java`
    - Execute `GetSightingHistoryForContent` query via `Fluxzero.queryAndWait(...)` and return `List<KeyValuePair>`
    - Return 404 if content does not exist
    - _Requirements: 9.1, 9.2, 9.3_

  - [x] 1.4 Write property test: SSE snapshot contains all current records (Property 1)
    - **Property 1: SSE snapshot contains all current WeightedAssociationState records**
    - Use jqwik to generate random sets of `WeightedAssociationState` records for a given `ContentId`
    - Verify the snapshot event contains exactly those records with `changeType` `INITIAL` and no records from other content IDs
    - **Validates: Requirements 1.2, 8.2**

  - [x] 1.5 Write property test: notification filtering by ContentId (Property 2)
    - **Property 2: Notification forwarding filters by ContentId and preserves payload**
    - Use jqwik to generate random `WeightedAssociationState` notifications with varying `contentId` values
    - Verify only matching notifications are forwarded with correct `changeType` and `payload` structure (full record for creates/updates, only `weightedAssociationId` for deletions)
    - **Validates: Requirements 1.3, 2.1, 2.2, 8.2, 8.3**

  - [x] 1.6 Write property test: monotonic event IDs (Property 3)
    - **Property 3: SSE event IDs are monotonically increasing**
    - Use jqwik to generate random sequences of SSE events sent to a single emitter
    - Verify each event's `id` field is strictly greater than the previous event's `id`
    - **Validates: Requirements 1.5**

  - [x] 1.7 Write property test: BigDecimal coordinate round-trip (Property 4)
    - **Property 4: BigDecimal coordinate serialization round-trip preserves precision**
    - Use jqwik to generate random `BigDecimal` lng/lat pairs
    - Serialize as JSON via `SseEventData` payload, deserialize back, verify `compareTo` equality
    - **Validates: Requirements 2.3**

- [x] 2. Rebuild TypeScript models
  - Run `./mvnw package -pl app` to regenerate TypeScript models so the frontend has access to `SseEventData` and any new types via `@trackrejoice/typescriptmodels`
  - _Requirements: 2.1_

- [x] 3. Checkpoint — Verify backend
  - Ensure all tests pass, ask the user if questions arise.

- [x] 4. Create frontend SSE infrastructure and models
  - [x] 4.1 Create `SseEvent` model
    - Create `ui/src/app/common/sse-event.model.ts`
    - Define `SseEvent` interface with `changeType: 'INITIAL' | 'CREATED' | 'UPDATED' | 'DELETED'` and `payload: WeightedAssociationState | { weightedAssociationId: string } | null`
    - _Requirements: 2.1_

  - [x] 4.2 Implement `SseService` with EventSource wrapper and reconnection logic
    - Create `ui/src/app/common/sse.service.ts`
    - Implement as `@Injectable({ providedIn: 'root' })` service
    - Expose `connect(url: string): Observable<SseEvent>` method
    - Create `EventSource` with `withCredentials: true`, listen for `snapshot` and `update` named events
    - On error, implement exponential backoff reconnection: 1s → 2s → 4s → ... → 30s max (`min(2^(n-1), 30)` seconds)
    - Track `Last-Event-ID` from received events and pass it on reconnect
    - Emit a `DISCONNECTED` status indicator while reconnecting
    - On reconnection success, request fresh snapshot to reconcile missed events
    - Close `EventSource` on Observable unsubscribe (tied to component `DestroyRef`)
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5_

  - [x] 4.3 Write property test: exponential backoff delay sequence (Property 9)
    - **Property 9: Exponential backoff delay sequence**
    - Use fast-check to generate random failure counts (1–50)
    - Verify delay equals `min(2^(n-1), 30)` seconds for each failure count
    - **Validates: Requirements 7.2**

- [x] 5. Implement dashboard shell and route registration
  - [x] 5.1 Create `PetContentDashboardComponent`
    - Create `ui/src/app/private/pet-content-dashboard/` directory
    - Implement `PetContentDashboardComponent` (selector: `track-rejoice-pet-content-dashboard`) as a standalone component
    - Read `id` from `ActivatedRoute` params
    - Fetch `Content` aggregate via `GET /api/content/{id}` with `{withCredentials: true}` on init
    - Pass `Content` to `ContentDetailsPanelComponent`, `contentId` and `lastConfirmedSighting` to `SightingHistoryMapComponent`, `contentId` to `ProposalsTableComponent`
    - Use CSS Grid layout: two columns top half, full-width bottom half
    - Show "Content not found" error message on 404 response
    - _Requirements: 3.1, 3.2, 3.3, 3.4_

  - [x] 5.2 Register `/contents/:id` route with `authGuard`
    - In `ui/src/app/app.routes.ts`, add route `{ path: 'contents/:id', component: PetContentDashboardComponent, canActivate: [authGuard] }` before the existing `contents` route
    - _Requirements: 3.3_

  - [x] 5.3 Write property test: route parameter propagation (Property 5)
    - **Property 5: Route parameter propagates to all dashboard panels**
    - Use fast-check to generate random `ContentId` strings
    - Verify the dashboard passes the exact string to all three child components as their `contentId` input
    - **Validates: Requirements 3.2**

- [x] 6. Implement content details panel
  - [x] 6.1 Create `ContentDetailsPanelComponent`
    - Create `ui/src/app/private/pet-content-dashboard/content-details-panel/` directory
    - Implement `ContentDetailsPanelComponent` (selector: `track-rejoice-content-details-panel`) as a standalone component
    - Accept `@Input() content: Content`
    - Check if `content.details` is of type `Pet` (check `@class` discriminator or type guard)
    - Render Pet fields: name, breed, gender, age, size, color, condition, description, image
    - Render content-level fields: online status, duration
    - If image is present, render `<img>` with alt text `"{name} - {breed}"`
    - If `ExtraDetails` type is not `Pet`, show fallback message: "Detailed view is only available for pet content"
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6_

  - [x] 6.2 Write property test: content details renders all required fields (Property 6)
    - **Property 6: Content details panel renders all required fields**
    - Use fast-check to generate random `Content` objects with `Pet` details
    - Verify all pet fields (name, breed, gender, age, size, color, condition, description), online status, and duration are rendered
    - Verify image alt text contains both pet name and breed when image is non-null
    - **Validates: Requirements 4.2, 4.3, 4.4, 4.6**

- [x] 7. Implement sighting history map panel
  - [x] 7.1 Create `SightingHistoryMapComponent`
    - Create `ui/src/app/private/pet-content-dashboard/sighting-history-map/` directory
    - Implement `SightingHistoryMapComponent` (selector: `track-rejoice-sighting-history-map`) as a standalone component
    - Accept `@Input() contentId: string` and `@Input() lastConfirmedSighting: SightingDetails | null`
    - Fetch sighting history via `GET /api/content/{contentId}/sighting-history` with `{withCredentials: true}`
    - Render Mapbox GL JS map with a marker at each sighting coordinate from the `KeyValuePair` list
    - Connect sequential markers with a polyline in chronological order (sorted by `key` timestamp)
    - On marker click, show popup with the sighting timestamp
    - If history has entries, fit map bounds to encompass all markers with padding
    - If history is empty but `lastConfirmedSighting` is provided, center map on those coordinates
    - If no location data at all, show placeholder message "No location data available"
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 5.7_

- [x] 8. Checkpoint — Verify dashboard shell and static panels
  - Ensure all tests pass, ask the user if questions arise.

- [x] 9. Implement proposals table with real-time SSE
  - [x] 9.1 Create `ProposalsTableComponent`
    - Create `ui/src/app/private/pet-content-dashboard/proposals-table/` directory
    - Implement `ProposalsTableComponent` (selector: `track-rejoice-proposals-table`) as a standalone component
    - Accept `@Input() contentId: string`
    - Connect to SSE endpoint via `SseService.connect('/api/content/{contentId}/proposals/stream')`
    - Use `TrackRejoiceDataSource<WeightedAssociationState>` to manage table data
    - On `snapshot` event, populate data source with all `INITIAL` records using `dataSource.set(...)`
    - On `update` event with `CREATED`: append new row
    - On `update` event with `UPDATED`: replace matching row by `weightedAssociationId`
    - On `update` event with `DELETED`: remove matching row by `weightedAssociationId`
    - Display Angular Material table with columns: `weightedAssociationId`, `sightingId`, `status`, `distance`, `score`
    - Apply CSS highlight animation (2 seconds) on rows after add or update
    - Show disconnection indicator when SSE connection is in disconnected state
    - Clean up SSE subscription on component destroy via `DestroyRef`
    - Follow existing `WeightedAssociationsComponent` patterns for table structure
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 7.1, 7.4_

  - [x] 9.2 Write property test: snapshot populates table correctly (Property 7)
    - **Property 7: Proposals table snapshot populates all rows with correct columns**
    - Use fast-check to generate random arrays of `WeightedAssociationState` records
    - Verify table displays exactly that many rows with correct `weightedAssociationId`, `sightingId`, `status`, `distance`, and `score` values
    - **Validates: Requirements 6.1, 6.2**

  - [x] 9.3 Write property test: table mutations produce correct final state (Property 8)
    - **Property 8: Proposals table mutations produce correct final state**
    - Use fast-check to generate random initial `WeightedAssociationState` arrays and random sequences of CREATED/UPDATED/DELETED mutation events
    - Verify final table state matches expected: CREATED appends, UPDATED replaces by `weightedAssociationId`, DELETED removes by `weightedAssociationId`
    - **Validates: Requirements 6.3, 6.4, 6.5**

- [x] 10. Final checkpoint — Verify complete dashboard
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation
- Property tests use jqwik (Java backend) and fast-check (TypeScript frontend) to validate universal correctness properties from the design document
- Unit tests validate specific examples and edge cases
- TypeScript models are auto-generated from Java records via `typescript-generator-maven-plugin`; run `./mvnw package -pl app` after backend changes
- The existing `TrackRejoiceDataSource` is reused for the proposals table data management
- Mapbox GL JS is already a project dependency from the content creation journey feature
- The `SseService` is a root-level singleton to allow reuse across components if needed
