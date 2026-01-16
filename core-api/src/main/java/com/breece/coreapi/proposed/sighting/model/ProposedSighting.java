package com.breece.coreapi.proposed.sighting.model;


import com.breece.coreapi.sighting.model.SightingDetails;
import com.breece.coreapi.sighting.model.SightingId;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;

//TODO: Consider pros/cons of dedicated Member entity
public record ProposedSighting(@EntityId ProposedSightingId proposedSightingId, @NotNull SightingId sightingId, @NotNull SightingDetails details) {
}
