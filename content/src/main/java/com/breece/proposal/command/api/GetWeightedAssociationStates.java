package com.breece.proposal.command.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.FacetedSearch;
import com.breece.coreapi.facets.Pagination;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.search.Search;

import java.util.List;

public record GetWeightedAssociationStates(List<FacetFilter> facetFilters, String filter, Pagination pagination) implements FacetedSearch<WeightedAssociationState, WeightedAssociationState> {

    @Override
    public Search getSearch(Sender sender) {
        return Fluxzero.search(WeightedAssociationState.class)
                .lookAhead(filter)
                .sortBy("score",false);
    }
}
/*public record GetWeightedAssociationStates(@NotNull @NotEmpty List<WeightedAssociationId> ids, @NotNull @NotEmpty List<WeightedAssociationStatus> statuses) implements Request<List<WeightedAssociationState>> {
    @HandleQuery
    List<WeightedAssociationState> find() {
        return Fluxzero.search(WeightedAssociationState.class)
                .any(ids.stream().map(v -> match(v, "weightedAssociationId")).toArray(Constraint[]::new))
                .any(statuses.stream().map(v -> match(v, "status")).toArray(Constraint[]::new))
                .fetchAll();
    }
}*/
