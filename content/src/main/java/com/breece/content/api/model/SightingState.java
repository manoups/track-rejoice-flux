package com.breece.content.api.model;

import com.breece.common.sighting.SightingContentBridge;
import com.breece.common.sighting.model.SightingId;
import com.breece.content.command.api.AcceptProposal;
import com.breece.content.command.api.ClaimSighting;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
@Consumer(name = "sighting-state-consumer", ignoreSegment = true)
public record SightingState(@Association @EntityId SightingId sightingId, @With boolean removeAfterMatching) {
    @HandleEvent
    static SightingState on(CreateSighting event) {
        return new SightingState(event.sightingId(), event.removeAfterMatching());
    }

    @HandleEvent(allowedClasses = {ClaimSighting.class, AcceptProposal.class})
    SightingState on(SightingContentBridge event) {
        if (removeAfterMatching) {
            Fluxzero.sendAndForgetCommand(new DeleteSighting(sightingId()));
        }
        return this;
    }

    @HandleEvent
    SightingState on(DeleteSighting event) {
        return null;
    }
}
