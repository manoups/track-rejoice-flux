package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.content.command.CreateProposal;
import com.breece.trackrejoice.content.command.RemoveMemberProposal;
import com.breece.trackrejoice.sighting.LinkSightingBackToContent;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "content-handler")
public class ContentHandler {
    @HandleEvent
    void on(ClaimSighting event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId(), event.sightingId()));
    }

    @HandleEvent
    void searchAndRemoveProposals(ClaimSighting event) {
        Fluxzero.loadAggregate(event.contentId()).get().proposedSightings().stream().filter(ps -> ps.sightingId().equals(event.sightingId()))
                .findFirst().ifPresent(ps -> Fluxzero.sendAndForgetCommand(new RemoveMemberProposal(ps.proposedSightingId())));
    }

    @HandleEvent
    void on(CreateProposal event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId(), event.sightingId()));
    }
}
