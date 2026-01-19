package com.breece.sighting.query.api;

import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
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
