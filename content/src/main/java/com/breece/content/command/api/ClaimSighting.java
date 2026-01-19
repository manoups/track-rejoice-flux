package com.breece.content.command.api;


import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.ConfirmedSightingUpdate;
import com.breece.content.api.SightingContentBridge;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.SightingDetails;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails) implements ActiveContentUpdate,
        ConfirmedSightingUpdate, SightingContentBridge {

    @AssertLegal
    void assertSightingExists() {
        if(Fluxzero.loadAggregate(sightingId).isEmpty()) {
            throw SightingErrors.notFound;
        }
    }

    @AssertLegal
    void assertDetailsCoherence() {
        if(!Fluxzero.loadAggregate(sightingId).get().details().equals(sightingDetails)) {
            throw SightingErrors.sightingMismatch;
        }
    }

    @Apply
    Content assign(Content content) {
        return content.withLastConfirmedSighting(sightingDetails);
    }
}
