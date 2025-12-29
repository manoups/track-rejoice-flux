package com.breece.trackrejoice.content;

import com.breece.trackrejoice.content.command.ClaimSighting;
import com.breece.trackrejoice.sighting.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class ContentEventHandler {
    @HandleEvent
    void on(ClaimSighting event) {
        Fluxzero.sendAndForgetCommand(new DeleteSighting(event.sightingId()));
    }
}
