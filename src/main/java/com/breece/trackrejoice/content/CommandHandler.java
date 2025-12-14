package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.CreateContent;
import com.breece.trackrejoice.sighting.api.ClaimSighting;
import com.breece.trackrejoice.sighting.api.CreateSighting;
import com.breece.trackrejoice.sighting.api.model.Sighting;
import com.breece.trackrejoice.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class CommandHandler {
    @HandleEvent
    void handle(CreateContent event) {
        Sighting sighting = Fluxzero.sendCommandAndWait(new CreateSighting(new SightingId(), event.details().getSighting().details().spottedLocation()));
        Fluxzero.sendAndForgetCommand(new ClaimSighting(event.contentId(), sighting.sightingId()));
    }
}
