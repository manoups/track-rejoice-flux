package com.breece.coreapi.facets;

import com.breece.coreapi.authentication.Sender;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.fluxzero.common.api.search.GetFacetStatsResult;
import io.fluxzero.sdk.persisting.search.Search;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.LocalHandler;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.concurrent.atomic.AtomicLong;

import static java.util.Optional.ofNullable;

public record GetFacets(@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) FacetedSearch<?,?> query) implements FacetableRequest, Request<GetFacetStatsResult> {
    private static final AtomicLong nextRequestId = new AtomicLong();
    @HandleQuery
    @LocalHandler(logMetrics = true)
    GetFacetStatsResult getFacets(Sender sender) {
        return ofNullable(getSearch(sender)).map(Search::facetStats).map(res -> new GetFacetStatsResult(nextRequestId.getAndIncrement(), res)).orElse(null);
    }
    @Override
    public Search getSearch(Sender sender) {
        return query.applyFacets(query.getSearch(sender));
    }
}
