package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record DeleteSighting(@NotNull SightingId sightingId) implements SightingUpdate {
    @Apply
    Sighting delete(Sighting sighting) {
        return null;
    }
}
