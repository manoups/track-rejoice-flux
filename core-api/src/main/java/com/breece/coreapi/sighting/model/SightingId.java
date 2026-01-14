package com.breece.coreapi.sighting.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Id;

public class SightingId extends Id<Sighting> {
    public SightingId() { this(Fluxzero.generateId());}
    public SightingId(String functionalId) {
        super(functionalId, "sighting-");
    }
}
