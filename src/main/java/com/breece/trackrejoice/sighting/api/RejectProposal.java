package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.ContentErrors;
import com.breece.trackrejoice.content.model.Content;
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
        if (Fluxzero.get()
                .aggregateRepository()
                .getAggregatesFor(sightingId).entrySet().stream().filter(entry -> entry.getValue() == Content.class)
                .map(entry -> Fluxzero.<Content>loadAggregateFor(entry.getKey(), entry.getValue()).get())
                .noneMatch(entry -> sender.isAuthorizedFor(entry.ownerId()))) {
            throw ContentErrors.unauthorized;
        }
    }

    @Apply
    Sighting reject(Sighting sighting) {
        return null;
    }
}
