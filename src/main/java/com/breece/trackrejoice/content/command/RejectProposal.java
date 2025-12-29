package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.sighting.api.SightingUpdate;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record RejectProposal(@NotNull SightingId sightingId) implements SightingUpdate {
    @AssertLegal
    void assertLinked() {
        Entity<Content> objectEntity = Fluxzero.loadAggregateFor(sightingId, Content.class);
        if (!objectEntity.isPresent()) {
            throw ContentErrors.notFound;
        }
    }

    @AssertLegal
    void assertAuthorized(Sender sender) {
        Content objectEntity = Fluxzero.<Content>loadAggregateFor(sightingId, Content.class).get();
        if (!sender.isAuthorizedFor(objectEntity.ownerId())) {
            throw ContentErrors.unauthorized;
        }
    }

    @Apply
    Sighting reject(Sighting sighting) {
        return null;
    }
}
