package com.breece.trackrejoice.sighting.api.model;

import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;

@Aggregate
public record ClaimedSighting(@EntityId SightingId sightingId, Sighting sighting) {
}
