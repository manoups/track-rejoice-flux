package com.breece.coreapi.facets;

import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.persisting.search.Search;

public interface FacetableRequest {
    Search getSearch(Sender sender);
}
