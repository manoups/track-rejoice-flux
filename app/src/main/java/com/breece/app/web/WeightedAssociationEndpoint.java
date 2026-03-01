package com.breece.app.web;

import com.breece.coreapi.facets.FacetPaginationRequestBody;
import com.breece.coreapi.facets.GetFacetStatsResult;
import com.breece.coreapi.facets.GetFacets;
import com.breece.proposal.command.api.GetWeightedAssociationStates;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/api/weighted/association")
public class WeightedAssociationEndpoint {
    @HandlePost(value = {"list", "list/"})
    List<WeightedAssociationState> getSightings(FacetPaginationRequestBody requestBody) {
        return Fluxzero.queryAndWait(new GetWeightedAssociationStates(requestBody.facetFilters(), requestBody.filter(), requestBody.pagination()));
    }

    @HandlePost(value = {"list/stats", "list/stats/"})
    GetFacetStatsResult getSightingStats(FacetPaginationRequestBody requestBody) {
        return Fluxzero.queryAndWait(new GetFacets(new GetWeightedAssociationStates(requestBody.facetFilters(), requestBody.filter(), requestBody.pagination())));
    }
}
