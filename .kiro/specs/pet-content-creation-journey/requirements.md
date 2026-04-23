# Requirements Document

## Introduction

The Pet Content Creation Journey is a two-step guided flow for creating lost or found pet content on Track Rejoice. In the first step, the user fills out a structured pet details form and selects a location on a Mapbox map to provide geospatial coordinates. Submitting the form creates a new Content aggregate in draft (offline) state. In the second step, the user completes a payment for the basic service via PayPal, which transitions the content from offline to online.

## Glossary

- **Content**: The primary aggregate representing a lost or found item. Has lifecycle states (ONLINE, OFFLINE). Created via the `CreateContent` command with `SightingDetails` and `ExtraDetails`.
- **Pet**: A subtype of `ExtraDetails` (via `MobileTarget`) representing a pet. Contains fields: name, breed, gender, age, size, color, condition, description, location, image. Requires a `SightingEnum` subtype of DOG or CAT.
- **SightingDetails**: A value object holding geospatial coordinates (`lng`, `lat` as `BigDecimal`).
- **Order**: An aggregate representing a purchase of services related to content. Created via the `CreateOrder` command with `ContentId`, `OrderDetails`, and a PSP reference.
- **Basic_Service**: A `Service` entity where `basic == true`. A completed order containing a Basic_Service triggers content publication (offline → online).
- **Pet_Details_Form**: The Angular form component where the user enters pet information and selects a map location.
- **Payment_Screen**: The Angular component where the user completes a PayPal payment for the Basic_Service.
- **Mapbox_Map**: The Mapbox GL JS map widget used to capture geospatial coordinates for the pet's last known location.
- **Content_Creation_Journey**: The multi-step Angular flow encompassing the Pet_Details_Form and the Payment_Screen.
- **PayPal_Checkout**: The PayPal payment integration used to process payments for orders.

## Requirements

### Requirement 1: Pet Details Form Display

**User Story:** As a user, I want to see a structured form for entering pet details, so that I can provide all relevant information about my lost or found pet.

#### Acceptance Criteria

1. WHEN the user navigates to the pet content creation route, THE Pet_Details_Form SHALL display input fields for: pet name, breed, gender (MALE or FEMALE), subtype (DOG or CAT), age, size, color, condition, description, and image.
2. THE Pet_Details_Form SHALL mark pet name, breed, gender, and subtype as required fields.
3. THE Pet_Details_Form SHALL render the gender field as a selectable control with options MALE and FEMALE.
4. THE Pet_Details_Form SHALL render the subtype field as a selectable control with options DOG and CAT.

### Requirement 2: Mapbox Location Selection

**User Story:** As a user, I want to select the last known location of my pet on a map, so that the platform can use accurate geospatial coordinates.

#### Acceptance Criteria

1. THE Pet_Details_Form SHALL display a Mapbox_Map widget for selecting the pet's last known location.
2. WHEN the user clicks on the Mapbox_Map, THE Pet_Details_Form SHALL capture the longitude and latitude from the click event and store them as SightingDetails coordinates.
3. WHEN the user selects a location on the Mapbox_Map, THE Pet_Details_Form SHALL display a marker at the selected position.
4. THE Pet_Details_Form SHALL treat the map location selection as a required field for form submission.

### Requirement 3: Form Validation

**User Story:** As a user, I want to receive clear validation feedback, so that I can correct any missing or invalid information before submitting.

#### Acceptance Criteria

1. WHEN the user attempts to submit the Pet_Details_Form with missing required fields, THE Pet_Details_Form SHALL prevent submission and display validation messages for each missing required field.
2. WHEN the user attempts to submit the Pet_Details_Form without selecting a location on the Mapbox_Map, THE Pet_Details_Form SHALL prevent submission and display a validation message indicating that a location is required.
3. WHILE the Pet_Details_Form has validation errors, THE Pet_Details_Form SHALL keep the submit button in a disabled state.

### Requirement 4: Draft Content Creation

**User Story:** As a user, I want to create a draft content entry when I submit the pet details form, so that my pet information is saved before I proceed to payment.

#### Acceptance Criteria

1. WHEN the user submits a valid Pet_Details_Form, THE Content_Creation_Journey SHALL send a `CreateContentDTO` payload to the `/api/content` endpoint containing the SightingDetails (longitude, latitude) and Pet ExtraDetails (with `@class` set to "Pet").
2. WHEN the `/api/content` endpoint receives a valid CreateContentDTO, THE Content_Creation_Journey SHALL create a new Content aggregate in OFFLINE status.
3. WHEN the Content aggregate is created successfully, THE Content_Creation_Journey SHALL receive the generated ContentId in the response.
4. WHILE the content creation request is in progress, THE Pet_Details_Form SHALL display a loading indicator and disable the submit button.
5. IF the content creation request fails, THEN THE Pet_Details_Form SHALL display an error message and allow the user to retry submission.

### Requirement 5: Navigation to Payment Screen

**User Story:** As a user, I want to be automatically directed to the payment screen after my pet content is created, so that I can complete the process without manual navigation.

#### Acceptance Criteria

1. WHEN the Content aggregate is created successfully, THE Content_Creation_Journey SHALL navigate the user to the Payment_Screen with the ContentId as a route parameter.
2. WHEN the user arrives at the Payment_Screen, THE Payment_Screen SHALL display the ContentId and a summary of the pet content that was created.

### Requirement 6: PayPal Payment Processing

**User Story:** As a user, I want to pay for the basic service via PayPal, so that my pet content goes live on the platform.

#### Acceptance Criteria

1. THE Payment_Screen SHALL display a PayPal_Checkout button for the Basic_Service associated with the content.
2. WHEN the user initiates payment via PayPal_Checkout, THE Payment_Screen SHALL create an Order by sending a `CreateOrder` request to the `/orders/{content-id}` endpoint with the Basic_Service ServiceId in the OrderDetails.
3. WHEN the PayPal_Checkout payment is approved by PayPal, THE Payment_Screen SHALL notify the backend by calling the payment accepted endpoint with the PSP reference.
4. WHILE the payment is being processed, THE Payment_Screen SHALL display a loading indicator and prevent duplicate payment submissions.
5. IF the PayPal_Checkout payment is rejected or cancelled by the user, THEN THE Payment_Screen SHALL display an error message and allow the user to retry payment.

### Requirement 7: Content Status Transition on Payment

**User Story:** As a user, I want my pet content to go online automatically after successful payment, so that other users can see my lost or found pet listing.

#### Acceptance Criteria

1. WHEN a PaymentAccepted event is processed for an Order containing a Basic_Service, THE OrderFulfillment handler SHALL send a `PublishContent` command for the associated ContentId.
2. WHEN the `PublishContent` command is applied, THE Content aggregate SHALL transition from OFFLINE to ONLINE status.
3. WHEN the content transitions to ONLINE status, THE Payment_Screen SHALL display a success confirmation to the user.

### Requirement 8: Authentication Guard

**User Story:** As a platform operator, I want only authenticated users to access the content creation journey, so that all content is associated with a verified user account.

#### Acceptance Criteria

1. THE Content_Creation_Journey SHALL require the user to be authenticated via OpenID Connect before accessing the Pet_Details_Form.
2. THE Content_Creation_Journey SHALL require the user to be authenticated via OpenID Connect before accessing the Payment_Screen.
3. WHEN an unauthenticated user attempts to access the Content_Creation_Journey, THE Content_Creation_Journey SHALL redirect the user to the authentication flow.

### Requirement 9: Journey Step Indicator

**User Story:** As a user, I want to see which step of the creation process I am on, so that I understand the overall flow and my progress.

#### Acceptance Criteria

1. THE Content_Creation_Journey SHALL display a step indicator showing two steps: "Pet Details" and "Payment".
2. WHILE the user is on the Pet_Details_Form, THE step indicator SHALL highlight the "Pet Details" step as active.
3. WHILE the user is on the Payment_Screen, THE step indicator SHALL highlight the "Payment" step as active.
