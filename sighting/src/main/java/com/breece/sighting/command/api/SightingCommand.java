package com.breece.sighting.command.api;

import com.breece.common.sighting.model.Sighting;
import com.breece.common.sighting.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.publishing.routing.RoutingKey;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface SightingCommand {
    @RoutingKey
    @NotNull
    SightingId sightingId();

    @HandleCommand
    default Sighting handle() {
        return Fluxzero.loadAggregate(sightingId()).assertAndApply(this).get();
    }
}
