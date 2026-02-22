package com.breece.coreapi.score.association;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.publishing.routing.RoutingKey;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface WeightedAssociationCommand {
    @RoutingKey
    @NotNull
    WeightedAssociationId weightedAssociationId();

    @HandleCommand
    default void handle(WeightedAssociationCommand command) {
        Fluxzero.loadEntity(command.weightedAssociationId()).assertAndApply(command);
    }
}
