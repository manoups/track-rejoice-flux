package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId) implements ContentUpdate {

    /*@AssertLegal
    void assertSightingExists() {
        if(Fluxzero.search(SightingState.class).match(sightingId, "sightingId").<SightingState>fetchFirst().isEmpty()) {
            throw SightingErrors.notFound;
        }
    }*/

    @Apply
    Content assign(Content content) {
        Sighting sighting = Fluxzero.loadEntity(sightingId).get();
        return content.withLastConfirmedSighting(sighting.details());
    }
}
