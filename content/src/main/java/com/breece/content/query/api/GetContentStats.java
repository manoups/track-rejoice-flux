package com.breece.content.query.api;

import com.breece.common.model.Content;
import io.fluxzero.common.api.search.FacetStats;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;

public record GetContentStats() implements Request<List<FacetStats>> {
    @HandleQuery
    List<FacetStats> getStats() {
        return Fluxzero.search(Content.class).facetStats();
    }
}
