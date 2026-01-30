package com.breece.proposal.api.model;

import com.breece.proposal.api.AcceptProposal;
import com.breece.proposal.api.CreateProposal;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
@Consumer(name = "linked-sighting-state-consumer", ignoreSegment = true)
public record LinkedSightingState(@Association @EntityId LinkedSightingId linkedSightingId,
                                  @With boolean removeAfterMatching, @With LinkedSightingStatus state) {
    @HandleEvent
    static LinkedSightingState on(CreateProposal event) {
        return new LinkedSightingState(event.linkedSightingId(), event.removeAfterMatching(), LinkedSightingStatus.CREATED);
    }

    @HandleEvent
    LinkedSightingState on(AcceptProposal event) {
        if (removeAfterMatching) {
            Fluxzero.sendAndForgetCommand(new DeleteSighting(event.sightingId()));
        }
        return withState(LinkedSightingStatus.ACCEPTED);
    }

    @HandleEvent
    LinkedSightingState on(DeleteSighting event) {
        if (state == LinkedSightingStatus.ACCEPTED) {
            return this;
        }
        return null;
    }
}
