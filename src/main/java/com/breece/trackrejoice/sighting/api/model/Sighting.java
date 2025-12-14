package com.breece.trackrejoice.sighting.api.model;

import com.breece.trackrejoice.content.model.ContentId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import lombok.With;
import org.locationtech.jts.geom.Geometry;

@Aggregate(searchable = true)
public record Sighting(@EntityId SightingId sightingId, Geometry spottedLocation, @With ContentId contentId) {
}
