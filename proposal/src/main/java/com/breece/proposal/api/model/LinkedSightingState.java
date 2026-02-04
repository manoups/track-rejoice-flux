package com.breece.proposal.api.model;

import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.*;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Stateful(commitInBatch = true)
@Consumer(name = "linked-sighting-state-consumer", ignoreSegment = true)
@Slf4j
public record LinkedSightingState(@EntityId LinkedSightingId linkedSightingId, SightingId sightingId,
                                  @With LinkedSightingStatus status) {
    @Association("linkedSightingId")
    @HandleEvent
    static LinkedSightingState on(CreateProposal event) {
        return new LinkedSightingState(event.linkedSightingId(), event.sightingId(), LinkedSightingStatus.CREATED);
    }

    @Association("linkedSightingId")
    @HandleEvent
    LinkedSightingState on(AcceptProposal event) {
        if(LinkedSightingStatus.CREATED != status()) {
            return this;
        }
        return withStatus(LinkedSightingStatus.ACCEPTED);
    }

    @Association("linkedSightingId")
    @HandleEvent
    LinkedSightingState on(RejectProposal event) {
        if(LinkedSightingStatus.CREATED != status()) {
            return this;
        }
        return withStatus(LinkedSightingStatus.REJECTED);
    }

    @Association("sightingId")
    @HandleEvent
    LinkedSightingState on(DeleteSighting event) {
        if(LinkedSightingStatus.ACCEPTED != status()) {
            Fluxzero.sendAndForgetCommand(new DeleteLinkedProposal(linkedSightingId()));
        }
        return this;
    }

    @Association("linkedSightingId")
    @HandleEvent
    LinkedSightingState on(DeleteLinkedProposal event) {
        return null;
    }
}
