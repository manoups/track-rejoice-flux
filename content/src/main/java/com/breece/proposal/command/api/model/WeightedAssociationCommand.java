package com.breece.proposal.command.api.model;

import jakarta.validation.constraints.NotNull;

public interface WeightedAssociationCommand {
    @NotNull
    WeightedAssociationId weightedAssociationId();
}
