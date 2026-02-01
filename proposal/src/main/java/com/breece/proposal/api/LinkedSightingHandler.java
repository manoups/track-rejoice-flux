package com.breece.proposal.api;

import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.model.LinkedSighting;
import com.breece.proposal.api.model.LinkedSightingStatus;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Consumer(name = "linked-sighting-handler")
public class LinkedSightingHandler {
    @HandleEvent
    void on(CreateProposal event, Sender sender) {
        if (sender.isAuthorizedFor(event.seeker())) {
            Fluxzero.sendAndForgetCommand(new AcceptProposal(event.linkedSightingId()));
        }
    }

    @HandleEvent
    void on(AcceptProposal event) {
        LinkedSighting linkedSighting = Fluxzero.loadAggregate(event.linkedSightingId()).get();
        Fluxzero.sendAndForgetCommand(new UpdateLastSeenPosition(linkedSighting.contentId(), linkedSighting.sightingDetails()));
    }

    @HandleEvent
    void on(DeleteSighting event) {
        List<LinkedSighting> linkedSightings = Fluxzero.queryAndWait(new GetLinkedSightingsBySightingIdAndStatuses(event.sightingId(), List.of(LinkedSightingStatus.CREATED, LinkedSightingStatus.REJECTED)));
        linkedSightings.forEach(linkedSighting -> Fluxzero.sendAndForgetCommand(new DeleteLinkedProposal(linkedSighting.linkedSightingId())));
    }
}
