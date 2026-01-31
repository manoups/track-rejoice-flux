package com.breece.proposal.api.model;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.publishing.routing.RoutingKey;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface LinkedSightingCommand {
    @RoutingKey
    @NotNull
    LinkedSightingId linkedSightingId();

    @HandleCommand
    default void handle(LinkedSightingCommand command) {
        Fluxzero.loadEntity(linkedSightingId()).assertAndApply(command);
    }
}
