# Requirements Document

## Introduction

The Pet Content SSE Dashboard is a new screen in the Track Rejoice Angular UI that provides a unified view of a pet content item. It replaces the existing unidirectional WebSocket push pattern (`UiUpdateSocketEndpoint`) with Server-Sent Events (SSE) for real-time updates. The dashboard is split into three panels: content details (top-left), sighting history on a Mapbox map (top-right), and a dynamically-updating table of proposed sightings (bottom half). The SSE stream delivers `WeightedAssociationState` changes so the proposals table stays current without polling.

## Glossary

- **Dashboard**: The Angular standalone component at route `/contents/:id` that composes the three panels described in this document.
- **Content_Details_Panel**: The top-left section of the Dashboard that displays Pet-specific fields from the Content aggregate.
- **Sighting_History_Panel**: The top-right section of the Dashboard that renders confirmed sighting history on a Mapbox GL JS map.
- **Proposals_Table**: The bottom-half section of the Dashboard that lists `WeightedAssociationState` records for the given Content and updates in real-time via SSE.
- **SSE_Endpoint**: A Spring Boot REST endpoint that produces a `text/event-stream` response carrying `WeightedAssociationState` change events scoped to a single Content.
- **SSE_Service**: An Angular service that wraps the browser `EventSource` API, manages connection lifecycle, and exposes an Observable of SSE events.
- **Content**: The domain aggregate identified by `ContentId`, containing `ExtraDetails` (polymorphic — `Pet` for pet-type content), `lastConfirmedSighting`, `weightedAssociations`, `ownerId`, `online` status, and `duration`.
- **Pet**: A subtype of `ExtraDetails` (via `MobileTarget`) with fields: name, breed, gender (`GenderEnum`), age, size, color, condition, description, location, image.
- **WeightedAssociationState**: A stateful consumer record with fields: `weightedAssociationId`, `contentId`, `sightingId`, `sightingDetails`, `status` (`CREATED`/`LINKED`/`ACCEPTED`/`REJECTED`), `distance`, `score`.
- **SightingDetails**: A value object containing `BigDecimal` lng and lat coordinates.
- **GetSightingHistoryForContent**: A FluxZero query that accepts a `ContentId` and returns a list of `KeyValuePair` (timestamp + `SightingDetails`) from the event store.
- **GetContent**: A FluxZero query that accepts a `ContentId` and returns the Content aggregate.
- **Sender**: The authenticated user context derived from the auth token, carrying a `UserId`.

## Requirements

### Requirement 1: SSE Endpoint for WeightedAssociationState Changes

**User Story:** As a content owner, I want to receive real-time updates about proposed sightings for my content, so that I can act on new matches without refreshing the page.

#### Acceptance Criteria

1. WHEN a client opens a GET request to `/api/content/{contentId}/proposals/stream`, THE SSE_Endpoint SHALL respond with content type `text/event-stream` and keep the connection open.
2. WHEN the SSE connection is established, THE SSE_Endpoint SHALL send the current list of `WeightedAssociationState` records for the given `ContentId` as an initial snapshot event.
3. WHEN a `WeightedAssociationState` associated with the given `ContentId` is created, updated, or deleted, THE SSE_Endpoint SHALL send an SSE event containing the change type and the full `WeightedAssociationState` payload.
4. THE SSE_Endpoint SHALL include an `event` field with value `snapshot` for the initial data load and `update` for subsequent change events.
5. THE SSE_Endpoint SHALL include a monotonically increasing `id` field on each SSE event so the client can track the last received event.
6. IF the authenticated user is not authorized to view the Content identified by `ContentId`, THEN THE SSE_Endpoint SHALL respond with HTTP 403 and close the connection.
7. IF the `ContentId` does not correspond to an existing Content aggregate, THEN THE SSE_Endpoint SHALL respond with HTTP 404.
8. WHEN the client closes the SSE connection, THE SSE_Endpoint SHALL release all resources associated with that subscription.

### Requirement 2: SSE Event Serialization

**User Story:** As a frontend developer, I want SSE events to follow a consistent JSON structure, so that I can parse and apply updates predictably.

#### Acceptance Criteria

1. THE SSE_Endpoint SHALL serialize each SSE event data field as a JSON object with properties: `changeType` (one of `INITIAL`, `CREATED`, `UPDATED`, `DELETED`), and `payload` (the `WeightedAssociationState` record or null for deletions).
2. WHEN a `WeightedAssociationState` is deleted, THE SSE_Endpoint SHALL set `changeType` to `DELETED` and include only the `weightedAssociationId` in the `payload`.
3. THE SSE_Endpoint SHALL serialize `BigDecimal` coordinate values in `SightingDetails` as JSON numbers without loss of precision.

### Requirement 3: Dashboard Route and Layout

**User Story:** As a user, I want to navigate to a pet content dashboard from the contents list, so that I can see all relevant information about a specific content item in one place.

#### Acceptance Criteria

1. WHEN a user navigates to `/contents/:id`, THE Dashboard SHALL load and display three panels: Content_Details_Panel (top-left), Sighting_History_Panel (top-right), and Proposals_Table (bottom half).
2. THE Dashboard SHALL use the `id` route parameter as the `ContentId` to fetch data for all three panels.
3. THE Dashboard SHALL be an Angular standalone component with selector `track-rejoice-pet-content-dashboard` and be protected by `authGuard`.
4. IF the Content identified by the route `id` does not exist, THEN THE Dashboard SHALL display an error message indicating the content was not found.

### Requirement 4: Content Details Panel

**User Story:** As a content owner, I want to see the pet details of my content item, so that I can verify the information is correct.

#### Acceptance Criteria

1. WHEN the Dashboard loads, THE Content_Details_Panel SHALL fetch the Content aggregate using the `GetContent` query via `GET /api/content/{contentId}`.
2. THE Content_Details_Panel SHALL display the Pet-specific fields: name, breed, gender, age, size, color, condition, description, and image.
3. THE Content_Details_Panel SHALL display the content-level fields: online status and duration.
4. WHEN the Content `ExtraDetails` type is `Pet`, THE Content_Details_Panel SHALL render the pet-specific layout with all Pet fields.
5. IF the Content `ExtraDetails` type is not `Pet`, THEN THE Content_Details_Panel SHALL display a fallback message indicating that detailed view is only available for pet content.
6. IF the image field is present, THEN THE Content_Details_Panel SHALL render the image with descriptive alt text derived from the pet name and breed.

### Requirement 5: Sighting History Map Panel

**User Story:** As a content owner, I want to see the confirmed sighting history of my content on a map, so that I can understand the movement pattern of my pet.

#### Acceptance Criteria

1. WHEN the Dashboard loads, THE Sighting_History_Panel SHALL fetch sighting history using the `GetSightingHistoryForContent` query via a REST call with the current `ContentId`.
2. THE Sighting_History_Panel SHALL render a Mapbox GL JS map displaying each historical sighting as a marker at the coordinates from `SightingDetails` (lng, lat).
3. THE Sighting_History_Panel SHALL connect sequential sighting markers with a polyline in chronological order based on the timestamp from `KeyValuePair.key`.
4. WHEN a user clicks a sighting marker, THE Sighting_History_Panel SHALL display a popup showing the timestamp of that sighting.
5. WHEN the sighting history contains at least one entry, THE Sighting_History_Panel SHALL fit the map bounds to encompass all sighting markers with appropriate padding.
6. IF the sighting history is empty, THEN THE Sighting_History_Panel SHALL center the map on the `lastConfirmedSighting` coordinates from the Content aggregate.
7. IF both sighting history and `lastConfirmedSighting` are absent, THEN THE Sighting_History_Panel SHALL display a placeholder message indicating no location data is available.

### Requirement 6: Proposals Table with Real-Time SSE Updates

**User Story:** As a content owner, I want to see proposed sighting matches update in real-time, so that I can quickly review and act on new proposals.

#### Acceptance Criteria

1. WHEN the Dashboard loads, THE Proposals_Table SHALL connect to the SSE_Endpoint at `/api/content/{contentId}/proposals/stream` and populate the table with the initial snapshot.
2. THE Proposals_Table SHALL display columns: `weightedAssociationId`, `sightingId`, `status`, `distance`, and `score`.
3. WHEN an SSE event with `changeType` `CREATED` is received, THE Proposals_Table SHALL append the new `WeightedAssociationState` row to the table.
4. WHEN an SSE event with `changeType` `UPDATED` is received, THE Proposals_Table SHALL update the matching row in-place identified by `weightedAssociationId`.
5. WHEN an SSE event with `changeType` `DELETED` is received, THE Proposals_Table SHALL remove the matching row identified by `weightedAssociationId`.
6. THE Proposals_Table SHALL visually highlight a row for 2 seconds after it is added or updated via an SSE event.
7. THE Proposals_Table SHALL use Angular Material table components consistent with the existing `WeightedAssociationsComponent`.

### Requirement 7: SSE Connection Lifecycle Management

**User Story:** As a user, I want the real-time connection to be managed reliably, so that I always see current data without manual intervention.

#### Acceptance Criteria

1. WHEN the Dashboard component is destroyed (user navigates away), THE SSE_Service SHALL close the active `EventSource` connection and release all subscriptions.
2. IF the SSE connection drops unexpectedly, THEN THE SSE_Service SHALL attempt to reconnect using exponential backoff starting at 1 second, doubling up to a maximum interval of 30 seconds.
3. WHEN the SSE_Service reconnects, THE SSE_Service SHALL include the `Last-Event-ID` header so the SSE_Endpoint can resume from the last received event.
4. WHILE the SSE connection is in a disconnected state, THE Proposals_Table SHALL display a visual indicator informing the user that real-time updates are temporarily unavailable.
5. WHEN the SSE connection is re-established after a disconnection, THE SSE_Service SHALL request a fresh snapshot to reconcile any missed events.

### Requirement 8: Backend SSE Infrastructure with FluxZero Notifications

**User Story:** As a backend developer, I want to leverage the existing FluxZero notification mechanism to feed the SSE stream, so that the implementation is consistent with the current event-sourcing architecture.

#### Acceptance Criteria

1. THE SSE_Endpoint SHALL use Spring Boot `SseEmitter` to produce the event stream.
2. THE SSE_Endpoint SHALL subscribe to FluxZero `@HandleNotification` events for `WeightedAssociationState` entity changes, filtering by the requested `ContentId`.
3. WHEN a `WeightedAssociationState` notification is received for a subscribed `ContentId`, THE SSE_Endpoint SHALL serialize the change and write it to the corresponding `SseEmitter`.
4. THE SSE_Endpoint SHALL authenticate the request using the same auth mechanism as other REST endpoints (Auth0 JWT via Zitadel).
5. IF the `SseEmitter` times out or errors, THEN THE SSE_Endpoint SHALL clean up the subscription and remove the emitter from the active connections registry.

### Requirement 9: Sighting History REST Endpoint

**User Story:** As a frontend developer, I want a REST endpoint to fetch sighting history for a content item, so that the map panel can load historical data.

#### Acceptance Criteria

1. WHEN a GET request is made to `/api/content/{contentId}/sighting-history`, THE ContentEndpoint SHALL execute the `GetSightingHistoryForContent` query and return the result as a JSON array of `KeyValuePair` objects.
2. IF the `ContentId` does not correspond to an existing Content aggregate, THEN THE ContentEndpoint SHALL respond with HTTP 404.
3. THE ContentEndpoint SHALL require authentication for the sighting history endpoint.
