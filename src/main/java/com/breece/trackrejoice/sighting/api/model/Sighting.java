package com.breece.trackrejoice.sighting.api.model;

import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import lombok.With;

/*TODO: Remove Aggregate after remove GetOpenSightings*/
@Aggregate(searchable = true)
public record Sighting(@EntityId SightingId sightingId, SightingDetails details, /*state:*/ @With boolean claimed) {
}
