package com.breece.coreapi.facets;

import jakarta.validation.Valid;

import java.util.List;

public record FacetPaginationRequestBody(String filter, List<FacetFilter> facetFilters, @Valid Pagination pagination) {
}
