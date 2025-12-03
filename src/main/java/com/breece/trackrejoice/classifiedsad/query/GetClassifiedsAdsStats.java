package com.breece.trackrejoice.classifiedsad.query;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAd;
import io.fluxzero.common.api.search.FacetStats;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

import java.util.List;

public record GetClassifiedsAdsStats() implements Request<List<FacetStats>> {
    @HandleQuery
    List<FacetStats> getStats() {
        return Fluxzero.search(ClassifiedsAd.class).facetStats();
    }
}
