package com.breece.content.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.LinkedSightingId;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingDetails;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.SightingUpdate;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record CreateProposalFacade(ContentId contentId, @NotNull SightingId sightingId, @NotNull LinkedSightingId linkedSightingId, @NotNull SightingDetails sightingDetails) implements SightingUpdate {
    @Apply
    Sighting on(Sighting sighting) {
        return sighting;
    }
}
