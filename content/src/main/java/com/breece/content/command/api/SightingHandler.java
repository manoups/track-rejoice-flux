package com.breece.content.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.SightingContentBridge;
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

    @HandleEvent(allowedClasses = {ClaimSighting.class, CreateProposal.class})
    void on(SightingContentBridge event) {
        Fluxzero.sendAndForgetCommand(new LinkSightingBackToContent(event.contentId().toString(), event.sightingId()));
    }

    @HandleEvent
    void on(DeleteSighting event) {
        Fluxzero.loadAggregate(event.sightingId()).previous().get().linkedContents().stream()
                .map(contentId -> Fluxzero.loadAggregate(contentId, Content.class))
                .filter(Entity::isPresent)
                .map(Entity::get)
                .flatMap(content -> content.linkedSightings().stream().filter(p -> Objects.equals(p.sightingId(), event.sightingId())))
                .forEach(proposal -> Fluxzero.sendAndForgetCommand(new RemoveMemberProposal(proposal.linkedSightingId())));
    }
}
