package com.breece.content.command.api.handler;

import com.breece.common.sighting.SightingAftermath;
import com.breece.sighting.command.api.DeleteSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class CleanupHandler {
    @HandleEvent
    void on(SightingAftermath event) {
        if (event.removeAfterMatching()) {
            Fluxzero.sendAndForgetCommand(new DeleteSighting(event.sightingId()));
        }
    }
}
