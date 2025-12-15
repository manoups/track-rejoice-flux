package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;

public record PostSighting(SightingId sightingId, SightingDetails sightingDetails) {
}
