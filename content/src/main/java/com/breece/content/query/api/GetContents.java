package com.breece.content.query.api;

import com.breece.content.api.model.Content;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.FacetedSearch;
import com.breece.coreapi.facets.Pagination;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.search.Search;
import io.fluxzero.sdk.persisting.search.SearchHit;

import java.util.List;
import java.util.stream.Stream;

public record GetContents(List<FacetFilter> facetFilters, String filter, Pagination pagination) implements FacetedSearch<Content, ContentDocument> {

    @Override
    public Search getSearch(Sender sender) {
        return Fluxzero.search(Content.class)
                .lookAhead(filter)
                .sortByTimestamp(true);
    }

    @Override
    public List<ContentDocument> fetch(Stream<SearchHit<Content>> stream, Sender sender) {
        return stream.map(ContentDocument::new).limit(pagination.pageSize()).toList();
    }
}
