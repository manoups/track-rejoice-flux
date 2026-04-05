package com.breece.proposal.command.api.model;

import com.breece.content.api.model.Content;
import com.breece.content.command.api.PublishContent;
import com.breece.proposal.command.api.CreateWeightedAssociation;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.command.api.CreateSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Consumer(name = "weighted-association-consumer")
public class WeightedAssociationHandler {
    @HandleEvent
    void on(PublishContent event, Content content) {
        Fluxzero.search(Sighting.class).matchFacet("subtype", List.of(content.details().getSubtype())).stream(Sighting.class)
                .map(sightingDocument ->
                        new CreateWeightedAssociation(content.contentId(), generator(), sightingDocument.sightingId(),
                                sightingDocument.details()))
                .map(Fluxzero::sendCommand)
                .forEach(CompletableFuture::join);
    }

    @HandleEvent
    void on(CreateSighting event, Sighting sighting) {
        Fluxzero.search(Content.class).matchFacet("details/subtype", List.of(sighting.subtype())).stream(Content.class)
                .map(content -> new CreateWeightedAssociation(content.contentId(), generator(), sighting.sightingId(), sighting.details()))
                .map(Fluxzero::sendCommand)
                .forEach(CompletableFuture::join);
    }

    private WeightedAssociationId generator() {
        return new WeightedAssociationId();
    }
}
