package com.breece.proposal.api;

import com.breece.sighting.command.api.LinkSightingBackToContent;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "sighting-handler")
public class SightingHandler {

    @HandleEvent(allowedClasses = {ClaimSighting.class, CreateProposal.class})
    void on(SightingContentBridge event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId().toString(), event.sightingId()));
    }
}
