package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.SightingUpdate;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull SightingId sightingId, @NotNull ContentId contentId) implements SightingUpdate {

    @Apply
    Sighting apply(Sighting sighting) {
        Fluxzero.sendAndForgetCommand(new ClaimSighting(contentId, sightingId));
        return sighting;
    }
}
