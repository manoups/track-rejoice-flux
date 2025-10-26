package com.breece.trackrejoice.classifiedsad.command;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

@TrackSelf
public interface ClassifiedsAdCommand {
    @NotNull
    ClassifiedsAdId classifiedsAdId();

    @HandleCommand
    default void handle(ClassifiedsAdCommand command) {
        Fluxzero.loadEntity(command.classifiedsAdId()).assertAndApply(command);
    }
}
