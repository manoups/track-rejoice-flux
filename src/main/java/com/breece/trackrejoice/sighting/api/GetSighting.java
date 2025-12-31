package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotNull;

public record GetSighting(@NotNull SightingId sightingId) implements Request<Sighting> {

    @HandleQuery
    Sighting find() {
        return Fluxzero.loadAggregate(sightingId).get();
    }
}
