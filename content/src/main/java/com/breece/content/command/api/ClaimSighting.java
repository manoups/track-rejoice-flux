package com.breece.content.command.api;


import com.breece.coreapi.content.model.Content;
import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.sighting.SightingContentBridge;
import com.breece.coreapi.sighting.SightingErrors;
import com.breece.coreapi.sighting.model.SightingDetails;
import com.breece.coreapi.sighting.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails) implements ContentUpdate, com.breece.coreapi.sighting.ConfirmedSightingUpdate, SightingContentBridge {

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
