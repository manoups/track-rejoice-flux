package com.breece.content.command.api;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "content-handler")
public class ContentHandler {
    @HandleEvent
    void searchAndRemoveProposals(ClaimSighting event) {
        Fluxzero.loadAggregate(event.contentId()).get().proposedSightings().stream().filter(ps -> ps.sightingId().equals(event.sightingId()))
                .findFirst().ifPresent(ps -> Fluxzero.sendAndForgetCommand(new RemoveMemberProposal(ps.proposedSightingId())));
    }
}
