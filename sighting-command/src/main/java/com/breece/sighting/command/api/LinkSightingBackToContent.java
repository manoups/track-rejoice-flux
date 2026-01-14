package com.breece.sighting.command.api;

import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.sighting.model.Sighting;
import com.breece.coreapi.sighting.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record LinkSightingBackToContent(@NotNull ContentId contentId, @NotNull SightingId sightingId) implements SightingInteract {
    @Apply
    Sighting link(Sighting sighting) {
        return sighting.toBuilder().linkedContent(contentId).build();
    }
}
