package com.breece.trackrejoice.sighting.api.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;

public record Sighting(@EntityId SightingId sightingId, @NotNull UserId owner, @NotNull SightingDetails details) {
}
