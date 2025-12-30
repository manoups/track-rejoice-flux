package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.sighting.SightingErrors;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingDetails;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record CreateSighting(@NotNull SightingId sightingId, @Valid @NotNull SightingDetails sightingDetails) implements SightingCommand {
    @AssertLegal
    void assertNew(Sighting sighting) {
        throw SightingErrors.alreadyExists;
    }

    @Apply
    Sighting create(Sender sender) {
        return new Sighting(sightingId, sender.userId(), sightingDetails);
    }
}
