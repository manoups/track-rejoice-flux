package com.breece.trackrejoice.sighting.api;

import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.content.command.CreateProposal;
import com.breece.trackrejoice.sighting.SightingState;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class SightingIndexer {
    @HandleEvent
    void on(CreateSighting event) {
        Fluxzero.index(Fluxzero.loadEntity(event.sightingId()));
    }

    @HandleEvent
    void on(ClaimSighting event) {
        Fluxzero.index(Fluxzero.loadEntity(event.sightingId()));
    }

    @HandleEvent
    void on(CreateProposal event) {
        Fluxzero.index(Fluxzero.loadEntity(event.sightingId()));
    }

    @HandleEvent
    void on(DeleteSighting event) {
        Fluxzero.deleteDocument(event.sightingId(), Sighting.class);
    }

}
