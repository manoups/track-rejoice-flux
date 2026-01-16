package com.breece.content.command.api;

import com.breece.coreapi.sighting.SightingContentBridge;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.command.api.LinkSightingBackToContent;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Consumer(name = "sighting-handler")
public class SightingHandler {

    @HandleEvent
    void on(SightingContentBridge event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId(), event.sightingId()));
    }

    @HandleEvent
    void on(DeleteSighting event) {
        Fluxzero.loadAggregate(event.sightingId()).previous().get().linkedContents().stream()
                .map(Fluxzero::loadAggregate)
                .filter(Entity::isPresent)
                .map(Entity::get)
                .filter(content -> content.proposedSightings().stream().anyMatch(p -> Objects.equals(p.sightingId(), event.sightingId())))
                .flatMap(content -> content.proposedSightings().stream().filter(p -> Objects.equals(p.sightingId(), event.sightingId())))
                .forEach(proposal -> Fluxzero.sendAndForgetCommand(new RemoveMemberProposal(proposal.proposedSightingId())));
    }
}
