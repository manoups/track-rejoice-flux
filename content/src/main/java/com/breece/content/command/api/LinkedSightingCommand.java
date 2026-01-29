package com.breece.content.command.api;

import com.breece.content.api.model.LinkedSightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface LinkedSightingCommand {
    @NotNull
    LinkedSightingId linkedSightingId();

    @HandleCommand
    default void handle(LinkedSightingCommand command) {
        Fluxzero.loadEntity(command.linkedSightingId()).assertAndApply(command);
    }
}
