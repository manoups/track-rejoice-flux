package com.breece.sighting.query.api;

import com.breece.sighting.api.model.Sighting;
import io.fluxzero.common.api.search.FacetStats;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;

public record GetSightingStats() implements Request<List<FacetStats>> {
    @HandleQuery
    List<FacetStats> getStats() {
        return Fluxzero.search(Sighting.class).facetStats();
    }
}

