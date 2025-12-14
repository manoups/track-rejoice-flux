package com.breece.trackrejoice.sighting.api.model;

import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.modeling.EntityId;
import lombok.With;
import org.locationtech.jts.geom.Geometry;

public record Sighting(@EntityId SightingId sightingId, Geometry spottedLocation, @With ContentId contentId) {
}
