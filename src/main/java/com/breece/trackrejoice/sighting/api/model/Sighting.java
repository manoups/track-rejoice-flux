package com.breece.trackrejoice.sighting.api.model;

import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;

/*TODO: Remove Aggregate after remove GetOpenSightings*/
@Aggregate(searchable = true)
public record Sighting(@EntityId SightingId sightingId, SightingDetails details) {
}
