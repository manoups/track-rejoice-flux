package com.breece.coreapi.facets;

import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.persisting.search.Search;
import io.fluxzero.sdk.persisting.search.SearchHit;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.LocalHandler;
import io.fluxzero.sdk.tracking.handling.Request;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public interface FacetedSearch<IN, OUT> extends Request<List<OUT>>, FacetableRequest {
    List<FacetFilter> facetFilters();

    Pagination pagination();

    default Search applyFacets(Search search) {
        if (Objects.isNull(search)) {
            return null;
        }
        if (CollectionUtils.isEmpty(facetFilters())) {
            return search;
        }
        for (var facet : facetFilters().stream().filter(facetFilter -> Objects.nonNull(facetFilter.values())).toList()) {
            search = search.matchFacet(facet.facetName(), facet.values());
        }
        return search;
    }

    @HandleQuery
    @LocalHandler(logMetrics = true)
    default List<OUT> handle(Sender sender) {
        Search search = applyFacets(getSearch(sender));
        if (Objects.isNull(search)) {
            return Collections.emptyList();
        }
        Integer from = ofNullable(pagination()).map(pagination -> pagination.page() * pagination.pageSize()).orElse(0);
        return fetch(search.skip(from).streamHits(count()), sender);
    }

    default List<OUT> fetch(Stream<SearchHit<IN>> stream, Sender sender) {
        return stream.map(tgt -> (OUT) tgt.getValue()).limit(count()).toList();
    }

    default Integer count() {
        return ofNullable(pagination()).map(Pagination::pageSize).orElse(10_000);
    }
}
