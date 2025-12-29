package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull SightingId sightingId, @NotNull ContentId contentId) implements ContentUpdate {

    @Apply
    void apply() {
        Fluxzero.sendAndForgetCommand(new ClaimSighting(contentId, sightingId));
    }
}
