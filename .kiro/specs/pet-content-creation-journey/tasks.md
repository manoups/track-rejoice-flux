# Implementation Plan: Pet Content Creation Journey

## Overview

Build a two-step Angular wizard for creating lost/found pet content with PayPal payment. The feature is primarily frontend (Angular components, Mapbox integration, PayPal checkout) with one small backend addition (basic service endpoint). All existing backend aggregates, commands, and fulfillment logic are reused as-is.

## Tasks

- [ ] 1. Add backend endpoint for basic service lookup
  - [x] 1.1 Implement `GET /api/services/basic` endpoint
    - Add a `@HandleGet` method in `app/src/main/java/com/breece/app/web/` (new `ServiceEndpoint` class or extend existing) that queries for the basic online service
    - Create a `GetBasicService` query record in `service/src/main/java/com/breece/service/api/` that implements `Request<Service>` and returns the `Service` where `basic == true` and `online == true`
    - Register the query handler in `ServiceHandler` using `@HandleQuery`
    - _Requirements: 6.1, 6.2_

  - [ ] 1.2 Write unit test for basic service endpoint
    - Test that `GET /api/services/basic` returns the correct service when a basic online service exists
    - Test that it returns an appropriate error when no basic online service exists
    - Add test in `app/src/test/java/com/breece/app/` following existing test patterns (e.g., `ContentEndpointTests.java`)
    - _Requirements: 6.1_

- [ ] 2. Rebuild TypeScript models
  - Run `./mvnw package -pl app` to regenerate TypeScript models from the new/updated Java records so the frontend has access to the `Service` type via `@trackrejoice/typescriptmodels`
  - _Requirements: 6.1, 6.2_

- [ ] 3. Checkpoint — Verify backend
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 4. Create journey shell and routing
  - [ ] 4.1 Create `ContentCreationJourneyComponent` with child router outlet
    - Create `ui/src/app/private/content-creation-journey/` directory
    - Implement `ContentCreationJourneyComponent` (selector: `track-rejoice-content-creation-journey`) as a standalone component with a `<router-outlet>` for child steps
    - Track `currentStep` (1 or 2) based on active child route
    - _Requirements: 9.1_

  - [ ] 4.2 Create `StepIndicatorComponent`
    - Create `ui/src/app/private/content-creation-journey/step-indicator/` directory
    - Implement `StepIndicatorComponent` (selector: `track-rejoice-step-indicator`) with `@Input() currentStep: number`
    - Display two steps: "Pet Details" and "Payment" with active highlighting based on `currentStep`
    - Use Angular Material stepper styling or Bootstrap progress indicator
    - _Requirements: 9.1, 9.2, 9.3_

  - [ ] 4.3 Register routes with auth guard
    - In `ui/src/app/app.routes.ts`, add route `/content/new` pointing to `ContentCreationJourneyComponent` with `canActivate: [authGuard]`
    - Add child routes: default child for `PetDetailsFormComponent`, and `payment/:contentId` for `PaymentScreenComponent`
    - _Requirements: 8.1, 8.2, 8.3_

  - [ ] 4.4 Write unit tests for routing and step indicator
    - Test that `StepIndicatorComponent` highlights step 1 when `currentStep` is 1 and step 2 when `currentStep` is 2
    - Test that `authGuard` is configured on the `/content/new` route
    - _Requirements: 8.1, 8.2, 9.2, 9.3_

- [ ] 5. Implement pet details form (Step 1)
  - [ ] 5.1 Create `PetDetailsFormComponent` with reactive form
    - Create `ui/src/app/private/content-creation-journey/pet-details-form/` directory
    - Implement `PetDetailsFormComponent` (selector: `track-rejoice-pet-details-form`) extending `View`
    - Build reactive form with `FormGroup` matching the design: `sightingDetails` group (`lng`, `lat` — both required) and `details` group (`@class: 'Pet'`, `subtype`, `name`, `breed`, `gender` — all required; `age`, `size`, `color`, `condition`, `description`, `location`, `image` — optional)
    - Render gender as a select with MALE/FEMALE options, subtype as a select with DOG/CAT options
    - Disable submit button when form is invalid; show validation messages for missing required fields
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 3.1, 3.2, 3.3_

  - [ ] 5.2 Create `MapboxLocationPickerComponent`
    - Create `ui/src/app/private/content-creation-journey/mapbox-location-picker/` directory
    - Implement `MapboxLocationPickerComponent` (selector: `track-rejoice-mapbox-location-picker`) as a standalone component
    - Accept `@Input() initialCenter?: [number, number]` for default map center
    - Render a Mapbox GL JS map; on click, place/move a marker and emit `{lng: number, lat: number}` via `@Output() locationSelected`
    - Include fallback message with manual lng/lat inputs if Mapbox fails to load
    - _Requirements: 2.1, 2.2, 2.3_

  - [ ] 5.3 Wire map picker into pet details form
    - Embed `MapboxLocationPickerComponent` in `PetDetailsFormComponent` template
    - On `locationSelected` event, patch `sightingDetails.lng` and `sightingDetails.lat` form controls with emitted coordinates
    - Show validation message when location is not selected on submit attempt
    - _Requirements: 2.2, 2.4, 3.2_

  - [ ] 5.4 Implement form submission with content creation
    - On valid form submit, POST `CreateContentDTO` payload to `/api/content` using `sendCommand` from the `View` base class
    - Show loading indicator and disable submit button while request is in progress
    - On success, receive `ContentId` and navigate to `/content/new/payment/{contentId}`
    - On error, display error alert and re-enable submit button for retry
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 5.1_

  - [ ] 5.5 Write property test: required field validation (Property 1)
    - **Property 1: Required field validation determines form submittability**
    - Use `fast-check` to generate random subsets of required fields (name, breed, gender, subtype, lng, lat) being null or present
    - Assert form is valid if and only if all required fields are non-null and non-empty
    - **Validates: Requirements 1.2, 2.4, 3.1, 3.2, 3.3**

  - [ ] 5.6 Write property test: map coordinates captured in form (Property 2)
    - **Property 2: Map click coordinates are captured in the form model**
    - Use `fast-check` to generate random `{lng, lat}` pairs
    - Assert that after emitting from `MapboxLocationPickerComponent`, the form model stores the exact values in `sightingDetails.lng` and `sightingDetails.lat`
    - **Validates: Requirements 2.2**

  - [ ] 5.7 Write property test: valid form produces correct payload (Property 3)
    - **Property 3: Valid form submission produces correct CreateContentDTO payload**
    - Use `fast-check` to generate random valid pet details and coordinates
    - Assert the HTTP POST payload matches the form values with `@class` set to `"Pet"`
    - **Validates: Requirements 4.1**

  - [ ] 5.8 Write property test: navigation with ContentId (Property 4)
    - **Property 4: Successful content creation navigates to payment with ContentId**
    - Use `fast-check` to generate random ContentId strings
    - Assert navigation target is `/content/new/payment/{contentId}`
    - **Validates: Requirements 5.1**

  - [ ] 5.9 Write unit tests for pet details form
    - Test form renders all expected fields (name, breed, gender, subtype, age, size, color, condition, description, image)
    - Test gender select has MALE/FEMALE options, subtype select has DOG/CAT options
    - Test map picker component is present in template
    - Test loading indicator appears during submission
    - Test error message displays on submission failure
    - _Requirements: 1.1, 1.3, 1.4, 2.1, 4.4, 4.5_

- [ ] 6. Checkpoint — Verify Step 1
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 7. Implement payment screen (Step 2)
  - [ ] 7.1 Create `PaymentScreenComponent` with content summary
    - Create `ui/src/app/private/content-creation-journey/payment-screen/` directory
    - Implement `PaymentScreenComponent` (selector: `track-rejoice-payment-screen`) extending `View`
    - Read `contentId` from route params
    - Display content summary (contentId and basic info)
    - Fetch basic service via `GET /api/services/basic` using `sendQuery` to obtain the `ServiceId`
    - Handle case where basic service is not found: show error message and disable PayPal button
    - _Requirements: 5.2, 6.1_

  - [ ] 7.2 Integrate PayPal checkout button
    - Dynamically load PayPal JS SDK script in `PaymentScreenComponent`
    - Render PayPal button that on approval: creates order via POST to `/orders/{contentId}` with `OrderDetails { serviceIds: [basicServiceId], updatedAt: now }`, then calls POST `/payments/paypal/accepted/{pspRef}` with the PSP reference
    - Show loading indicator during payment processing; prevent duplicate submissions
    - On payment rejection/cancellation, show informational message and keep PayPal button active for retry
    - On payment error, show error alert and allow retry
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

  - [ ] 7.3 Display success confirmation
    - After successful payment callback, show success confirmation message indicating the content is now online
    - _Requirements: 7.3_

  - [ ] 7.4 Write property test: order creation sends correct ServiceId (Property 5)
    - **Property 5: Order creation sends correct Basic_Service ServiceId**
    - Use `fast-check` to generate random ContentId and ServiceId values
    - Assert the order creation payload contains the basic service's `ServiceId` in the `serviceIds` list
    - **Validates: Requirements 6.2**

  - [ ] 7.5 Write unit tests for payment screen
    - Test PayPal button renders when basic service is available
    - Test content summary is displayed with contentId
    - Test loading state during payment processing
    - Test error/retry message on payment rejection
    - Test success confirmation after payment
    - Test error message when basic service is not found
    - _Requirements: 5.2, 6.1, 6.4, 6.5, 7.3_

- [ ] 8. Final checkpoint — Verify complete journey
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- The backend already handles content creation, ordering, payment acceptance, and order fulfillment — only the basic service lookup endpoint is new
- TypeScript models are auto-generated from Java records via `typescript-generator-maven-plugin`; run `./mvnw package -pl app` after backend changes
- Property tests use `fast-check` and validate universal correctness properties from the design document
- Unit tests validate specific UI rendering scenarios and edge cases
