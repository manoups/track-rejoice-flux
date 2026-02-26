package com.breece.proposal.command.api.model;

import com.breece.content.command.api.ContentInteract;
import com.breece.proposal.command.api.WeightedProposalErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface WeightedAssociationUpdate extends LinkedSightingCommand, ContentInteract {
    @AssertLegal
    default void assertExists(@Nullable WeightedAssociation weightedAssociation) {
        if (Objects.isNull(weightedAssociation)) {
            throw WeightedProposalErrors.notFound;
        }
    }
}
