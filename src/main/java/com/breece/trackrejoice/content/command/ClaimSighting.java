package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.SightingContentBridge;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails) implements ContentUpdate, SightingContentBridge {

    @AssertLegal
    void assertSightingExists() {
        if(Fluxzero.loadAggregate(sightingId).isEmpty()) {
            throw SightingErrors.notFound;
        }
    }

    @AssertLegal
    void assertCoherence() {
        if(!Fluxzero.loadAggregate(sightingId).get().details().equals(sightingDetails)) {
            throw SightingErrors.sightingMismatch;
        }
    }

    @Apply
    Content assign(Content content) {
        return content.withLastConfirmedSighting(sightingDetails);
    }
}
