package com.breece.proposal.command.api.model;

import com.breece.content.command.api.ContentInteract;
import com.breece.proposal.command.api.WeightedAssociationErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface WeightedAssociationUpdate extends WeightedAssociationCommand, ContentInteract {
    @AssertLegal
    default void assertExists(@Nullable WeightedAssociation weightedAssociation) {
        if (Objects.isNull(weightedAssociation)) {
            throw WeightedAssociationErrors.notFound;
        }
    }
}
