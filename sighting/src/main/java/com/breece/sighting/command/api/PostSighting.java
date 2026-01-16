package com.breece.sighting.command.api;

import com.breece.coreapi.sighting.model.SightingDetails;
import com.breece.coreapi.sighting.model.SightingId;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;

@RequiresUser
public record PostSighting(SightingId sightingId, SightingDetails sightingDetails) {
}
