package com.breece.proposal.api;

import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "linked-sighting-handler")
public class LinkedSightingHandler {
    @HandleEvent
    void on(CreateProposal event, Sender sender) {
        if (sender.isAuthorizedFor(event.seeker())) {
            Fluxzero.sendAndForgetCommand(new AcceptProposal(event.linkedSightingId(), event.sightingDetails(), event.contentId(), event.sightingId()));
        }
    }

    @HandleEvent
    void on(AcceptProposal event) {
        Fluxzero.sendAndForgetCommand(new UpdateLastSeenPosition(event.contentId(), event.sightingDetails()));
    }
}
