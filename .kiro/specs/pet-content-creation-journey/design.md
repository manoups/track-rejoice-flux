# Design Document

## Overview

The Pet Content Creation Journey is a two-step Angular wizard that guides authenticated users through creating a lost/found pet listing and paying for the basic service to publish it. Step 1 collects pet details and a map-pinned location, then creates a Content aggregate in OFFLINE status via the existing `CreateContent` command. Step 2 presents a PayPal checkout for the Basic_Service; on successful payment the existing `OrderFulfillment` handler publishes the content (OFFLINE → ONLINE).

The backend already has all the necessary aggregates, commands, and fulfillment logic. This feature is primarily a frontend effort — building the Angular wizard components, integrating Mapbox GL JS for location selection, and wiring PayPal checkout — with minimal backend changes (a new endpoint to fetch the basic service).

## Architecture

```mermaid
flowchart TD
    subgraph Angular UI
        A[ContentCreationJourneyComponent] --> B[PetDetailsFormComponent]
        A --> C[PaymentScreenComponent]
        B --> D[MapboxLocationPickerComponent]
        C --> E[PayPal Checkout Button]
    end

    subgraph Backend - Existing
        F[ContentEndpoint POST /api/content]
        G[OrdersEndpoint POST /orders/{content-id}]
        H[PspCallbackEndpoint POST /payments/paypal/accepted/{pspRef}]
        I[OrderFulfillment]
        J[ServiceEndpoint GET /api/services/basic]
    end

    B -->|CreateContentDTO| F
    F -->|ContentId| B
    B -->|navigate with ContentId| C
    C -->|OrderDetails| G
    E -->|PayPal approval| H
    H -->|PaymentAccepted event| I
    I -->|PublishContent command| K[Content Aggregate OFFLINE→ONLINE]
```

### Key Design Decisions

1. **Frontend-heavy feature**: The backend already supports content creation, ordering, payment acceptance, and order fulfillment. We reuse all existing commands and event handlers.
2. **Wrapper journey component**: A parent `ContentCreationJourneyComponent` manages step state and routes between the two child steps using Angular child routes.
3. **Mapbox as a standalone component**: The map picker is extracted into a reusable `MapboxLocationPickerComponent` that emits `{lng, lat}` coordinates via an `@Output`.
4. **PayPal JS SDK loaded dynamically**: The PayPal button script is loaded on-demand in the payment screen to avoid blocking initial page load.
5. **New backend endpoint for basic service**: A small addition to expose `GET /api/services/basic` so the frontend can fetch the `ServiceId` needed for the order without hardcoding it.

## Components and Interfaces

### Angular Components

#### ContentCreationJourneyComponent (Parent)
- **Selector**: `track-rejoice-content-creation-journey`
- **Route**: `/content/new`
- **Responsibility**: Hosts the step indicator and child router outlet. Tracks current step (1 = Pet Details, 2 = Payment).
- **Auth**: Protected by `authGuard` from `@edgeflare/ngx-oidc`.

#### PetDetailsFormComponent (Step 1)
- **Selector**: `track-rejoice-pet-details-form`
- **Route**: `/content/new` (default child)
- **Responsibility**: Reactive form with fields: name (required), breed (required), gender (required, select: MALE/FEMALE), subtype (required, select: DOG/CAT), age, size, color, condition, description, image. Embeds `MapboxLocationPickerComponent` for coordinates. On valid submit, POSTs `CreateContentDTO` to `/api/content`, receives `ContentId`, navigates to step 2.
- **Form model**:
  ```typescript
  form = new FormGroup({
    sightingDetails: new FormGroup({
      lng: new FormControl<number>(null, [Validators.required]),
      lat: new FormControl<number>(null, [Validators.required])
    }),
    details: new FormGroup({
      '@class': new FormControl('Pet'),
      subtype: new FormControl<string>(null, [Validators.required]),
      name: new FormControl<string>(null, [Validators.required]),
      breed: new FormControl<string>(null, [Validators.required]),
      gender: new FormControl<string>(null, [Validators.required]),
      age: new FormControl<string>(null),
      size: new FormControl<string>(null),
      color: new FormControl<string>(null),
      condition: new FormControl<string>(null),
      description: new FormControl<string>(null),
      location: new FormControl<string>(null),
      image: new FormControl<string>(null),
    })
  });
  ```

#### MapboxLocationPickerComponent
- **Selector**: `track-rejoice-mapbox-location-picker`
- **Responsibility**: Renders a Mapbox GL JS map. On click, places/moves a marker and emits `{lng: number, lat: number}` via `@Output() locationSelected`.
- **Inputs**: `@Input() initialCenter?: [number, number]` for default map center.

#### PaymentScreenComponent (Step 2)
- **Selector**: `track-rejoice-payment-screen`
- **Route**: `/content/new/payment/:contentId`
- **Responsibility**: Displays content summary, loads the basic `ServiceId` from `/api/services/basic`, renders PayPal checkout button. On PayPal approval: creates order via `/orders/{contentId}`, then calls `/payments/paypal/accepted/{pspRef}`. Shows success confirmation when content goes online.

#### StepIndicatorComponent
- **Selector**: `track-rejoice-step-indicator`
- **Responsibility**: Displays a two-step progress indicator. Accepts `@Input() currentStep: number`.

### Backend Addition

#### ServiceEndpoint (new endpoint)
- **Path**: `GET /api/services/basic`
- **Returns**: The `Service` record where `basic == true` and `online == true`.
- **Location**: New `@HandleGet` method in existing `ServiceEndpoint` class or a new endpoint in `app/web/`.

### API Contracts

#### POST /api/content (existing)
- **Request**: `CreateContentDTO { sightingDetails: { lng, lat }, details: { @class: "Pet", subtype, name, breed, gender, ... } }`
- **Response**: `ContentId` (the generated ID)

#### POST /orders/{content-id} (existing)
- **Request**: `OrderDetails { serviceIds: [basicServiceId], updatedAt: now }`
- **Response**: void (order created)

#### POST /payments/paypal/accepted/{pspReference} (existing)
- **Request**: empty body, PSP reference in path
- **Response**: void (triggers `PaymentAccepted` event → `OrderFulfillment` → `PublishContent`)

#### GET /api/services/basic (new)
- **Response**: `Service { serviceId, serviceDetails, basic: true, online: true }`

## Data Models

### Existing Models (no changes)

| Model | Module | Key Fields |
|---|---|---|
| `Content` | content | `contentId`, `lastConfirmedSighting`, `details` (ExtraDetails), `ownerId`, `online`, `duration` |
| `Pet` extends `MobileTarget` extends `ExtraDetails` | content | `name`, `breed`, `gender`, `subtype`, `age`, `size`, `color`, `condition`, `description`, `location`, `image` |
| `SightingDetails` | core-api | `lng: BigDecimal`, `lat: BigDecimal` |
| `Order` | order | `orderId`, `userId`, `contentId`, `details` (OrderDetails), `paymentReference` |
| `OrderDetails` | order | `serviceIds: List<ServiceId>`, `updatedAt: Instant` |
| `Service` | service | `serviceId`, `serviceDetails`, `basic: boolean`, `online: boolean` |
| `GenderEnum` | content | `MALE`, `FEMALE` |
| `SightingEnum` | core-api | `DOG`, `CAT`, ... |
| `ContentStatus` | content | `ONLINE`, `OFFLINE` |

### Auto-Generated TypeScript Models

The `typescript-generator-maven-plugin` already generates TypeScript interfaces for all Java records above into `@trackrejoice/typescriptmodels`. The frontend will import these directly — no manual TypeScript model creation needed.


## Correctness Properties

*A property is a characteristic or behavior that should hold true across all valid executions of a system — essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.*

### Property 1: Required field validation determines form submittability

*For any* combination of values for the pet details form fields (name, breed, gender, subtype, lng, lat), the form SHALL be valid and submittable if and only if all required fields (name, breed, gender, subtype, lng, lat) are non-null and non-empty. When any required field is missing, the submit button SHALL be disabled and submission SHALL be prevented.

**Validates: Requirements 1.2, 2.4, 3.1, 3.2, 3.3**

### Property 2: Map click coordinates are captured in the form model

*For any* longitude and latitude pair emitted by the MapboxLocationPickerComponent, the Pet_Details_Form SHALL store those exact coordinate values in the `sightingDetails.lng` and `sightingDetails.lat` form controls.

**Validates: Requirements 2.2**

### Property 3: Valid form submission produces correct CreateContentDTO payload

*For any* valid set of pet details (name, breed, gender, subtype, and optional fields) and valid coordinates (lng, lat), submitting the form SHALL produce an HTTP POST to `/api/content` with a `CreateContentDTO` payload where `sightingDetails.lng` and `sightingDetails.lat` match the form coordinates, and `details` contains all pet fields with `@class` set to `"Pet"`.

**Validates: Requirements 4.1**

### Property 4: Successful content creation navigates to payment with ContentId

*For any* `ContentId` returned by a successful POST to `/api/content`, the Content_Creation_Journey SHALL navigate to the payment route `/content/new/payment/{contentId}` with that exact ContentId as the route parameter.

**Validates: Requirements 5.1**

### Property 5: Order creation sends correct Basic_Service ServiceId

*For any* `ContentId` and `ServiceId` (where the service is the basic service), initiating payment SHALL produce an HTTP POST to `/orders/{contentId}` with an `OrderDetails` payload containing the basic service's `ServiceId` in the `serviceIds` list.

**Validates: Requirements 6.2**

## Error Handling

### Frontend Error Handling

| Scenario | Behavior |
|---|---|
| Content creation HTTP error (4xx/5xx) | Display error alert via the existing alerting component. Re-enable submit button. Allow retry. |
| PayPal payment rejected by user | Display informational message ("Payment was cancelled"). Keep PayPal button active for retry. |
| PayPal payment error (network/server) | Display error alert. Re-enable PayPal button for retry. |
| Order creation HTTP error | Display error alert. Allow user to retry payment. |
| Mapbox GL JS fails to load | Display fallback message with manual lng/lat input fields. |
| Basic service not found (GET /api/services/basic returns empty) | Display error message indicating the service is unavailable. Disable PayPal button. |

### Backend Error Handling

All backend error handling is already implemented in the existing commands:

| Scenario | Existing Behavior |
|---|---|
| `CreateContent` with invalid DTO | Jakarta validation rejects with 400 |
| `CreateOrder` for non-existent content | `OrderErrors.productNotFound` |
| `CreateOrder` without basic service for OFFLINE content | `ServiceErrors.basicServiceRequired` |
| `CreateOrder` for non-existent service | `OrderErrors.serviceNotFound` |
| `PaymentAccepted` with unknown reference | Order not found, no fulfillment triggered |

## Testing Strategy

### Unit Tests (Example-Based)

Focus on specific scenarios and UI rendering:

- **PetDetailsFormComponent**: Verify form renders all expected fields (1.1), gender select has MALE/FEMALE options (1.3), subtype select has DOG/CAT options (1.4), map picker is present (2.1), marker appears on map click (2.3), loading indicator during submission (4.4), error message on failure (4.5).
- **PaymentScreenComponent**: Verify PayPal button renders (6.1), content summary displayed (5.2), loading state during payment (6.4), error/retry on rejection (6.5), success confirmation (7.3).
- **StepIndicatorComponent**: Verify two steps displayed (9.1), correct step highlighted per route (9.2, 9.3).
- **Route configuration**: Verify `authGuard` on journey routes (8.1, 8.2).

### Property-Based Tests

Property-based testing applies to this feature for the form validation logic and data transformation layer. Use `fast-check` as the PBT library for TypeScript/Angular tests.

Each property test MUST:
- Run a minimum of 100 iterations
- Reference the design property via tag comment
- Use `fast-check` arbitraries to generate random form inputs

Properties to implement:
1. **Property 1**: Generate random subsets of required fields being null/present → form validity matches all-required-present predicate.
2. **Property 2**: Generate random `{lng, lat}` pairs → verify form model captures exact values.
3. **Property 3**: Generate random valid pet details → verify HTTP payload structure matches form values.
4. **Property 4**: Generate random ContentId strings → verify navigation target matches expected route.
5. **Property 5**: Generate random ContentId and ServiceId → verify order creation payload.

### Integration Tests

- **Content creation flow** (4.2, 4.3): POST `CreateContentDTO` to `/api/content`, verify Content aggregate created with `online=false` and ContentId returned.
- **Order fulfillment flow** (7.1, 7.2): Verify `PaymentAccepted` event triggers `PublishContent` and content transitions to ONLINE.
- **Auth guard** (8.3): Verify unauthenticated access redirects to auth flow.

### Smoke Tests

- **Route guard presence** (8.1, 8.2): Verify `authGuard` is configured on `/content/new` routes.
