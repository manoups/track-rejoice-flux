package com.breece.sighting.command.api;

import com.breece.common.sighting.SightingErrors;
import com.breece.common.sighting.model.Sighting;
import com.breece.common.sighting.model.SightingDetails;
import com.breece.common.sighting.model.SightingId;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RequiresUser
public record CreateSighting(@NotNull SightingId sightingId, @Valid @NotNull SightingDetails sightingDetails, boolean removeAfterMatching) implements SightingCommand {
    @AssertLegal
    void assertNew(Sighting sighting) {
        throw SightingErrors.alreadyExists;
    }

    @Apply
    Sighting create(Sender sender) {
        return new Sighting(sightingId, sender.userId(), sightingDetails, List.of());
    }
}
