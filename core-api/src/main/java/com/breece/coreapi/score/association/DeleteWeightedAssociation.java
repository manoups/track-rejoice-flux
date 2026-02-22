package com.breece.coreapi.score.association;

import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.annotation.Nullable;

public record DeleteWeightedAssociation(WeightedAssociationId weightedAssociationId) implements WeightedAssociationCommand {
    @AssertLegal
    void assertExists(@Nullable WeightedAssociation weightedAssociation) {
        if (weightedAssociation == null) {
            throw new IllegalStateException("Score association does not exist");
        }
    }

    @Apply
    WeightedAssociation apply(WeightedAssociation weightedAssociation) {
        return null;
    }
}
