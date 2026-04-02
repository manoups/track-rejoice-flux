package com.breece.proposal.command.api.model;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentStatus;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.GetContentStates;
import com.breece.content.command.api.PublishContent;
import com.breece.content.query.api.ContentDocument;
import com.breece.content.query.api.GetContents;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.Pagination;
import com.breece.proposal.command.api.CreateWeightedAssociation;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.query.api.GetSightings;
import com.breece.sighting.query.api.SightingDocument;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Consumer(name = "weighted-association-consumer", ignoreSegment = true)
@Slf4j
public class WeightedAssociationHandler {
    @HandleEvent
    void on(PublishContent event, Content content) {
        List<SightingDocument> sightingDocuments = Fluxzero.queryAndWait(new GetSightings(List.of(FacetFilter.builder().facetName("subtype").values(List.of(content.details().getSubtype())).build()), "", new Pagination(0, 1_000_000)));
        sightingDocuments.stream().map(sightingDocument ->
                new CreateWeightedAssociation(content.contentId(), generator(), sightingDocument.sightingId(),
                        sightingDocument.details())
        ).map(Fluxzero::sendCommand).forEach(CompletableFuture::join);
    }

    @HandleEvent
    void on(CreateSighting event, Sighting sighting) {
        List<ContentDocument> contentDocuments =
                Fluxzero.queryAndWait(new GetContents(List.of(FacetFilter.builder().facetName("details/subtype").values(List.of(sighting.subtype())).build()), "", new Pagination(0, 1_000_000)));
        if (!contentDocuments.isEmpty()) {
            List<ContentState> contentStates = Fluxzero.queryAndWait(new GetContentStates(contentDocuments.stream().map(ContentDocument::contentId).toList(), List.of(ContentStatus.ONLINE)));
            contentDocuments
                    .stream()
                    .filter(it -> contentStates.stream().anyMatch(state -> state.contentId().equals(it.contentId())))
                    .map(contentDocument ->
                            new CreateWeightedAssociation(contentDocument.contentId(), generator(), sighting.sightingId(),
                                    sighting.details())
                    ).map(Fluxzero::sendCommand).forEach(CompletableFuture::join);
        }
    }

    public WeightedAssociationId generator() {
        return new WeightedAssociationId();
    }
}
