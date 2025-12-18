package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;

@RequiresUser
public record PostSighting(SightingId sightingId, SightingDetails sightingDetails) {
}
