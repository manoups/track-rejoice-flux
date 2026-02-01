package com.breece.proposal.api.model;

import com.breece.proposal.api.AcceptProposal;
import com.breece.proposal.api.CreateProposal;
import com.breece.proposal.api.DeleteLinkedProposal;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.SightingUpdate;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Stateful
@Consumer(name = "linked-sighting-state-consumer", ignoreSegment = true)
@Slf4j
public record LinkedSightingState(@EntityId @Association LinkedSightingId linkedSightingId, SightingId sightingId,
                                  @With LinkedSightingStatus status) {
    @HandleEvent
    static LinkedSightingState on(CreateProposal event) {
        return new LinkedSightingState(event.linkedSightingId(), event.sightingId(), LinkedSightingStatus.CREATED);
    }

    @HandleEvent
    LinkedSightingState on(AcceptProposal event) {
        return withStatus(LinkedSightingStatus.ACCEPTED);
    }

    @HandleEvent
    LinkedSightingState on(SightingUpdate event) {
        log.info("Handling SightingUpdate for LinkedSightingId: {}", linkedSightingId);
        return this;
    }

    @HandleEvent
    LinkedSightingState on(DeleteLinkedProposal event) {
        return null;
    }
}
