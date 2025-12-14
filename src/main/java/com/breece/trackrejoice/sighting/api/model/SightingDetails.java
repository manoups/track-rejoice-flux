package com.breece.trackrejoice.sighting.api.model;

import org.locationtech.jts.geom.Geometry;

//Value Object
public record SightingDetails(Geometry spottedLocation) {
}
