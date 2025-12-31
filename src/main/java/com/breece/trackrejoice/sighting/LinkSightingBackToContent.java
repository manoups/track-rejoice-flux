package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.SightingInteract;
import com.breece.trackrejoice.sighting.api.SightingUpdate;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record LinkSightingBackToContent(@NotNull ContentId contentId, @NotNull SightingId sightingId) implements SightingInteract {
    @Apply
    Sighting link(Sighting sighting) {
        return sighting.toBuilder().linkedContent(contentId).build();
    }
}
