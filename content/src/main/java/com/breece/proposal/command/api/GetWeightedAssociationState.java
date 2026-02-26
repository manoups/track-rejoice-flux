package com.breece.proposal.command.api;

import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;

public record GetWeightedAssociationState(WeightedAssociationId weightedAssociationId) implements Request<WeightedAssociationState> {

    @HandleQuery
    WeightedAssociationState get() {
        return Fluxzero.search(WeightedAssociationState.class)
                .match(weightedAssociationId, "weightedAssociationId")
                .<WeightedAssociationState>fetchFirst().orElse(null);
    }
}
