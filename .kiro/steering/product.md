# Product Overview

Track Rejoice is a lost-and-found platform built on the FluxZero event-sourcing framework.

## Core Domain Concepts

- **Content**: Items that are lost or found — the primary aggregate. Has lifecycle states (draft, published, online, offline, cancelled).
- **Sighting**: User-reported observations of a lost/found item, linked to content via weighted associations.
- **Weighted Association / Proposal**: A scored link between a sighting and content. Proposals can be created, accepted, rejected, or claimed. Associations carry distance/score calculations.
- **Order**: Represents a purchase of services related to content. Has a 1-1 relationship with payment (payment is a value object). Orders are many-to-one with content. Digital goods — cancellation is not supported.
- **Service**: Purchasable services attached to content (basic subscription, extras).
- **User**: User profiles with role-based access control (ADMIN, MANAGER, USER).

## Key Business Rules

- A basic subscription order on content with an active subscription extends the duration.
- Orders bind content with service entities.
- A completed order on a basic product changes content status to online.
- Sightings can be removed after matching.
- Content going offline deletes all related weighted associations.
- Payment status is inherited by the order (1-1 relationship).
