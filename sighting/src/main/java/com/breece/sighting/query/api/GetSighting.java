package com.breece.sighting.query.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleQuery;
import io.fluxzero.sdk.tracking.handling.Request;
import jakarta.validation.constraints.NotNull;

public record GetSighting(@NotNull SightingId sightingId) implements Request<Sighting> {

    @HandleQuery
    Sighting find(Sender sender) {
        return Fluxzero.search(Sighting.class)
                .match(sender.isAdmin() ? null : sender.userId(), "ownerId")
                .match(sightingId, "sightingId")
                .fetchFirstOrNull();
    }
}
