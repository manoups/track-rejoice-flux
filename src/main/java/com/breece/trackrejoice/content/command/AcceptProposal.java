package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ContentId;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record AcceptProposal(@NotNull SightingId sightingId, @NotNull ContentId contentId) implements ContentUpdate {

    @Apply
    Content apply() {
        return Fluxzero.sendCommandAndWait(new ClaimSighting(contentId, sightingId));
    }
}
