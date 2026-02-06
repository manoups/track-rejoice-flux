# FluxZero Basic Java Example

A foundational FluxZero application demonstrating core concepts including user management, role-based authentication, and REST/WebSocket APIs using Java and Spring Boot.

## What's Included

This example demonstrates:

- **User Management System**: Complete CRUD operations for user profiles
- **Role-Based Authentication**: User roles (ADMIN, MANAGER, USER) with permission checking
- **REST API Endpoints**: HTTP endpoints for user operations
- **Event Sourcing**: FluxZero's event-driven architecture
- **Command Query Separation**: Separate command and query handlers
- **Spring Boot Integration**: Full Spring ecosystem integration

### Key Components

- **Users API** (`com.breece.trackrejoice.user.*`): User creation, profile management, role assignment
- **Authentication** (`com.breece.trackrejoice.authentication.*`): Role-based access control
- **API Models**: Request/response objects and domain models
- **Command Handlers**: Business logic for user operations
- **Query Handlers**: Data retrieval and projection logic

## Running the Application

### Prerequisites

- Java 24 or higher
- Maven 3.6.3+ or Gradle 8.0+

### Quick Start

Start the complete application stack (Test Server + Proxy + Application) with a single command:

**Using Gradle:**
```bash
./gradlew runTestApp
```

**Using Maven:**
```bash
mvn exec:java
```

**Using IntelliJ IDEA:**
- Use the provided run configurations to start the TestApp directly from the IDE

This will start:
- **FluxZero Test Server** on port 8888
- **FluxZero Proxy** on port 8080
- **Spring Boot Application** on port 8080 (proxied)

## API Endpoints

Once running, try these endpoints:

- **GET** `/api/users` - List all users
- **POST** `/api/users` - Create new user
- **PUT** `/api/users/{id}/role` - Set user role
- **GET** `/api/users/{id}` - Get user details

## Testing

Run the test suite:
```bash
./gradlew test
# or
mvn test
```

## Architecture

This example follows FluxZero's event-driven architecture:

1. **Commands** trigger business operations
2. **Events** capture state changes
3. **Queries** project current state
4. **Event Store** provides persistence and audit trail
5. **Projections** maintain read-optimized views

## Next Steps

- Explore the source code in `src/main/java/com/breece/trackrejoice/`
- Check out the test examples in `src/test/java/`
- Modify user roles and permissions
- Add new API endpoints
- Implement custom business logic


### Orders
- Comparison to shopping cart?
- Contain multiple services for one product
- Need to validate that a basic subscription is already active or included in the basket
- A basic subcription order on content with active subscription extends the duration of the subscription 
- An order binds together a content with entities

### Depedencies loops
- Content -> linkedSightingId -> ProposedSighting -> SightingId -> Sighting -> ContentId -> Content
- If we remove the sighting holding ContentId's the cycle is broken
- Payment does not need to be an aggregate -- it can be a simple payment reference string
- ~~An order can be an aggregate member of content. We can use projections for "My orders" screen.~~
- We should keep order as in independent aggregate with many-to-one relationship with content
- A product can have multiple orders
  - an order of a basic product will change its status to online upon completed order
- There is a 1-1 relationship between an order and a payment. Thus, a payment will become a value object and the status will be inherited by the order.
- Since this is digital goods, cancelling an order is not possible. We may later create a workflow to support it


## Questions
- If an aggregate is stored only in the document store, does it implicitly impact the event publication strategy to publish-only? What is the purpose of storing the events?
- Does performance decrease if many objects match on the association member variable but there is no handler for the action?
- Can I load the state in the context?
- Can typeFilter be tested?
- What does `disableScheduledCommandHandler` do?
- Decider pattern