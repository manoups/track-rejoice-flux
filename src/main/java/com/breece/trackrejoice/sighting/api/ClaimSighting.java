package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.SightingState;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId) implements SightingUpdate {
    @AssertLegal
    void assertContentOwnership(Sender sender) {
        Entity<Content> contentEntity = Fluxzero.loadAggregate(contentId);
        if (!contentEntity.isPresent() || !contentEntity.get().ownerId().equals(sender.userId())) {
            throw ContentErrors.notFound;
        }
    }

    @AssertLegal
    void assertSightingNotAssigned(Sighting sighting) {
        Fluxzero.search(SightingState.class).match(sightingId, "sightingId").<SightingState>fetchFirst().ifPresent(
                state -> {
                    if (state.isClaimed()) {
                        throw SightingErrors.alreadyClaimed;
                    }
                });
    }

    @Apply
    Sighting assign(Sighting sighting) {
        return sighting;
    }
}
