package com.breece.proposal.command.api.model;

import com.breece.content.command.api.ContentInteract;
import com.breece.proposal.command.api.GetWeightedAssociationState;
import com.breece.proposal.command.api.WeightedAssociationErrors;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;

import java.util.Objects;

public interface WeightedAssociationUpdate extends WeightedAssociationCommand, ContentInteract {
    @AssertLegal
    default void assertExists() {
        if (Objects.isNull(Fluxzero.queryAndWait(new GetWeightedAssociationState(weightedAssociationId())))) {
            throw WeightedAssociationErrors.notFound;
        }
    }
}
