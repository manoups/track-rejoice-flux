package com.breece.trackrejoice.sighting;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

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
        if (Objects.nonNull(sighting.contentId())) {
            throw SightingErrors.alreadyClaimed;
        }
    }

    @Apply
    Sighting assign(Sighting sighting) {
        return sighting.withContentId(contentId);
    }
}
