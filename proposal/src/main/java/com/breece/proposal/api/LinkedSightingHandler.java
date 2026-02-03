package com.breece.proposal.api;

import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "linked-sighting-handler")
public class LinkedSightingHandler {
    @HandleEvent
    void on(CreateProposal event, Sender sender) {
        if (sender.isAuthorizedFor(event.seeker())) {
            Fluxzero.publishEvent(new AcceptProposal(event.linkedSightingId()));
        }
    }

    @HandleEvent
    void on(AcceptProposal event) {
        LinkedSighting linkedSighting = Fluxzero.loadAggregate(event.linkedSightingId()).get();
        Fluxzero.sendAndForgetCommand(new UpdateLastSeenPosition(linkedSighting.contentId(), linkedSighting.sightingDetails()));
        Fluxzero.loadAggregate(linkedSighting.sightingId())
                .mapIfPresent(Entity::get)
                .filter(Sighting::removeAfterMatching)
                .map(Sighting::sightingId)
                .map(DeleteSighting::new)
                .ifPresent(Fluxzero::sendAndForgetCommand);
    }
}
