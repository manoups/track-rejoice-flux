package com.breece.content.command.api;

import com.breece.coreapi.proposed.sighting.model.ProposedSightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface ProposedSightingCommand {
    @NotNull
    ProposedSightingId proposedSightingId();

    @HandleCommand
    default void handle(ProposedSightingCommand command) {
        Fluxzero.loadEntity(command.proposedSightingId()).assertAndApply(command);
    }
}
