package com.breece.trackrejoice.sighting.api.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;

@Aggregate(searchable = true)
public record Sighting(@EntityId SightingId sightingId, @NotNull UserId source, @NotNull SightingDetails details) {
}
