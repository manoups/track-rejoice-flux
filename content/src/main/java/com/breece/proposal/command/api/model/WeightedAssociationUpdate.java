package com.breece.proposal.command.api.model;

import com.breece.content.api.model.WeightedAssociation;
import com.breece.content.command.api.ContentInteract;
import com.breece.proposal.command.api.WeightedAssociationErrors;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.validation.constraints.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

public interface WeightedAssociationUpdate extends ContentInteract {
    @NotNull
    WeightedAssociationId weightedAssociationId();

    @AssertLegal
    default void assertExists(@Nullable WeightedAssociation weightedAssociation) {
        if (Objects.isNull(weightedAssociation)) {
            throw WeightedAssociationErrors.notFound;
        }
    }
}
