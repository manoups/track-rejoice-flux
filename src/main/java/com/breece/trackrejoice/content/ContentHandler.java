package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.content.command.CreateProposal;
import com.breece.trackrejoice.sighting.LinkSightingBackToContent;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class ContentHandler {
    @HandleEvent
    void on(ClaimSighting event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId(), event.sightingId()));
    }

    @HandleEvent
    void on(CreateProposal event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId(), event.sightingId()));
    }
}
