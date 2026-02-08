package com.breece.sighting.command.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.common.SightingEnum;
import com.breece.sighting.api.SightingErrors;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.tracking.handling.authentication.RequiresUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequiresUser
public record CreateSighting(@NotNull SightingId sightingId, @Valid @NotNull SightingDetails sightingDetails, boolean removeAfterMatching, @Facet @NotNull SightingEnum subtype) implements SightingCommand {
    public CreateSighting(CreateSightingDTO payload) {
        this(new SightingId(), payload.sightingDetails(), payload.removeAfterMatching(), payload.subtype());
    }

    @AssertLegal
    void assertNew(Sighting sighting) {
        throw SightingErrors.alreadyExists;
    }

    @Apply
    Sighting create(Sender sender) {
        return new Sighting(sightingId, sender.userId(), sightingDetails, removeAfterMatching, subtype);
    }
}
