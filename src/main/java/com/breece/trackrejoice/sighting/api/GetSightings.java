package com.breece.trackrejoice.sighting.api;


import com.breece.trackrejoice.sighting.api.model.Sighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record GetSightings(@PositiveOrZero Integer page, @Positive Integer pageSize,
                           String filter) implements Request<List<Sighting>> {

    public GetSightings() {
        this(0, 10, null);
    }

    @HandleQuery
    List<Sighting> getSightings() {
        return Fluxzero.search(Sighting.class)
                .sortBy("sightingId")
                .skip(page * pageSize)
                .fetch(pageSize);
    }
}
