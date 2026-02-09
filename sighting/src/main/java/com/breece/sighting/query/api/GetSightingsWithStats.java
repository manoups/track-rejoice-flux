package com.breece.sighting.query.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.FacetedSearch;
import com.breece.coreapi.facets.Pagination;
import com.breece.sighting.api.model.Sighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.search.Search;
import io.fluxzero.sdk.persisting.search.SearchHit;

import java.util.List;
import java.util.stream.Stream;

public record GetSightingsWithStats(List<FacetFilter> facetFilters, String filter, Pagination pagination) implements FacetedSearch<Sighting, SightingDocument> {

    @Override
    public Search getSearch(Sender sender) {
        return Fluxzero.search(Sighting.class)
                .lookAhead(filter)
                .sortByTimestamp(true);
    }

    @Override
    public List<SightingDocument> fetch(Stream<SearchHit<Sighting>> stream, Sender sender) {
        return stream.map(SightingDocument::new).toList();
    }
}