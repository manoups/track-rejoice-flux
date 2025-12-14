package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface SightingCommand {
    @NotNull
    SightingId sightingId();

    @HandleCommand
    default void handle() {
        Fluxzero.loadAggregate(sightingId()).assertAndApply(this);
    }
}
