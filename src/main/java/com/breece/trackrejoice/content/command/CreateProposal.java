package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record CreateProposal(@NotNull SightingId sightingId, @NotNull ContentId contentId) implements ContentInteract {
    @AssertLegal
    void assertSightingExists() {
        if (!Fluxzero.loadAggregate(sightingId).isPresent()) {
            throw SightingErrors.notFound;
        }
    }

    @AssertLegal
    void assertOwner(Sighting sighting, Sender sender) {
        if (!sender.isAuthorizedFor(sighting.owner())) {
            throw SightingErrors.notOwner;
        }
    }

    @Apply
    Sighting propose(Sighting sighting) {
        return new Sighting(sightingId, sighting.owner(), sighting.details());
    }
}
