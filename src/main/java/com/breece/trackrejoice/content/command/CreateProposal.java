package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record CreateProposal(ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails) implements ContentInteract {
    @AssertLegal
    void assertSightingExists() {
        if (!Fluxzero.loadAggregate(sightingId).isPresent()) {
            throw SightingErrors.notFound;
        }
    }

    @AssertLegal
    void assertOwner(Sender sender) {
        Sighting sighting = Fluxzero.loadAggregate(sightingId).get();
        if (!sender.isAuthorizedFor(sighting.owner())) {
            throw SightingErrors.notOwner;
        }
    }

    @Apply
    Sighting propose(Sender sender) {
        return new Sighting(sightingId, sender.userId(), sightingDetails);
    }
}
