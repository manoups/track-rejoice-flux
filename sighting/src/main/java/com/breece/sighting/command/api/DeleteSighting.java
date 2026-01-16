package com.breece.sighting.command.api;

import com.breece.common.sighting.model.Sighting;
import com.breece.common.sighting.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record DeleteSighting(@NotNull SightingId sightingId) implements SightingUpdate {
    @Apply
    Sighting delete(Sighting sighting) {
        return null;
    }
}
