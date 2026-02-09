package com.breece.coreapi.facets;

import jakarta.validation.Valid;

import java.util.List;

public record FacetPaginationRequestBody(List<FacetFilter> facetFilters, String filter, @Valid Pagination pagination) {
}
