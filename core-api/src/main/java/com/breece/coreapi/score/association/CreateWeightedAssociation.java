package com.breece.coreapi.score.association;

import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;

public record CreateWeightedAssociation(@EntityId WeightedAssociationId weightedAssociationId, @NotBlank String contentId, @NotBlank String sightingId) implements WeightedAssociationCommand {
    @AssertLegal
    void unique(WeightedAssociation weightedAssociation) {
        throw new IllegalStateException("Weighted association already exists");
    }

    @Apply
    WeightedAssociation apply() {
        return new WeightedAssociation(weightedAssociationId, contentId, sightingId);
    }
}
