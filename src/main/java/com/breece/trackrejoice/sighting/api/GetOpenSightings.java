package com.breece.trackrejoice.sighting.api;


import com.breece.trackrejoice.sighting.SightingState;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import io.fluxzero.common.api.search.Constraint;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Collections;
import java.util.List;

import static io.fluxzero.common.api.search.constraints.MatchConstraint.match;

public record GetOpenSightings(@PositiveOrZero Integer page, @Positive Integer pageSize,
                               String filter) implements Request<List<Sighting>> {

    public GetOpenSightings() {
        this(0, 10, null);
    }

    @HandleQuery
    List<Sighting> getSightings() {
        List<SightingState> fetch = Fluxzero.search(SightingState.class)
                .match(false, "claimed")
                .sortBy("id")
                .skip(page * pageSize)
                .fetch(pageSize);
        return fetch.isEmpty() ? Collections.emptyList() : Fluxzero.search(Sighting.class)
                .any(fetch.stream().map(res -> match(res.getSightingId(), "sightingId")).toArray(Constraint[]::new))
                .fetchAll();
    }
}
