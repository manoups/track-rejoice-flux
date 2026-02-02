package com.breece.proposal.api.model;

import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.*;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
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
public record LinkedSightingState(@EntityId @Association LinkedSightingId linkedSightingId, @Association SightingId sightingId,
                                  @With LinkedSightingStatus status) {
    @HandleEvent
    static LinkedSightingState on(CreateProposal event, Sender sender) {
        if (sender.isAuthorizedFor(event.seeker())) {
            Fluxzero.sendAndForgetCommand(new AcceptProposal(event.linkedSightingId()));
        }
        return new LinkedSightingState(event.linkedSightingId(), event.sightingId(), LinkedSightingStatus.CREATED);
    }

    @HandleEvent
    LinkedSightingState on(AcceptProposal event, LinkedSighting linkedSighting) {
        if (status() != LinkedSightingStatus.CREATED) {
            throw LinkedSightingErrors.incorrectState;
        }
        Fluxzero.sendAndForgetCommand(new UpdateLastSeenPosition(linkedSighting.contentId(), linkedSighting.sightingDetails()));
        Fluxzero.sendAndForgetCommand(new UpdateStatusProjection(linkedSightingId(), LinkedSightingStatus.ACCEPTED));
        if(Fluxzero.loadAggregate(linkedSighting.sightingId()).get().removeAfterMatching()) {
            Fluxzero.sendAndForgetCommand(new DeleteSighting(linkedSighting.sightingId()));
        }
        return withStatus(LinkedSightingStatus.ACCEPTED);
    }

    @HandleEvent
    LinkedSightingState on(RejectProposal event) {
        if (status() != LinkedSightingStatus.CREATED) {
            throw LinkedSightingErrors.incorrectState;
        }
        Fluxzero.sendAndForgetCommand(new UpdateStatusProjection(linkedSightingId(), LinkedSightingStatus.REJECTED));
        return withStatus(LinkedSightingStatus.REJECTED);
    }

    @HandleEvent
    LinkedSightingState on(DeleteSighting event, Entity<Sighting> sightingEntity) {
        if(LinkedSightingStatus.ACCEPTED != status()) {
            Fluxzero.sendAndForgetCommand(new DeleteLinkedProposal(linkedSightingId()));
        }
        return this;
    }

    @HandleEvent
    LinkedSightingState on(DeleteLinkedProposal event) {
        return null;
    }
}
