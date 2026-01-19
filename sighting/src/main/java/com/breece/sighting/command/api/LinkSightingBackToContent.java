package com.breece.sighting.command.api;

import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record LinkSightingBackToContent(@NotNull String contentId, @NotNull SightingId sightingId) implements SightingInteract {
    @Apply
    Sighting link(Sighting sighting) {
        return sighting.toBuilder().linkedContent(contentId).build();
    }
}
