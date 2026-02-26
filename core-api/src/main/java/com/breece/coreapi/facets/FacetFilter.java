package com.breece.coreapi.facets;

import lombok.Builder;

import java.util.Arrays;
import java.util.List;

@Builder
public record FacetFilter(String facetName, List<Object> values) {
    public FacetFilter(String facetName, Object... value) {
        this(facetName, Arrays.asList(value));
    }
}
