package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.SightingState;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import com.breece.trackrejoice.sighting.api.model.SightingStatus;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId) implements ContentUpdate {

    @AssertLegal
    void assertSightingExists() {
        if(Fluxzero.search(SightingState.class).match(sightingId, "sightingId").<SightingState>fetchFirst().isEmpty()) {
            throw SightingErrors.notFound;
        }
    }
    @AssertLegal
    void assertSightingNotClaimed() {
        Fluxzero.search(SightingState.class).match(sightingId, "sightingId").<SightingState>fetchFirst().ifPresent(
                state -> {
                    if (state.getStatus() == SightingStatus.CLAIMED) {
                        throw SightingErrors.alreadyClaimed;
                    }
                });
    }

    @Apply
    Content assign(Content content) {
        return Fluxzero.sendCommandAndWait(new UpdateContent(contentId, content.details()));
    }
}
