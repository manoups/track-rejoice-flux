package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;

//TODO: Consider pros/cons of dedicated Member entity
public record ProposedSighting(@EntityId ProposedSightingId proposedSightingId, @NotNull SightingId sightingId, @NotNull SightingDetails details) {
}
