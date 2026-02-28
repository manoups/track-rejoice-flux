package com.breece.proposal.command.api.model;

import com.breece.content.api.model.ContentStatus;
import com.breece.content.command.api.ContentState;
import com.breece.content.command.api.GetContentStates;
import com.breece.content.command.api.PublishContent;
import com.breece.content.query.api.ContentDocument;
import com.breece.content.query.api.GetContents;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.Pagination;
import com.breece.proposal.command.api.CreateWeightedAssociation;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.query.api.GetSightings;
import com.breece.sighting.query.api.SightingDocument;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Consumer(name = "weighted-association-handler")
public class WeightedAssociationHandler {
    @HandleEvent
    void on(PublishContent event) {
        Fluxzero.loadAggregate(event.contentId()).mapIfPresent(Entity::get).ifPresent(
                content ->
                {
                    List<SightingDocument> sightingDocuments = Fluxzero.queryAndWait(new GetSightings(List.of(FacetFilter.builder().facetName("subtype").values(List.of(content.details().getSubtype())).build()), "", new Pagination(0, 1_000_000)));
                    for (SightingDocument sightingDocument : sightingDocuments) {
                        Fluxzero.sendAndForgetCommand(new CreateWeightedAssociation(content.contentId(), new WeightedAssociationId(content.contentId(), sightingDocument.sightingId()), sightingDocument.sightingId(), sightingDocument.details()));
                    }
                });
    }

    @HandleEvent
    void on(CreateSighting event) {
        Fluxzero.loadAggregate(event.sightingId()).mapIfPresent(Entity::get).ifPresent(
                sighting ->
                {
                    List<ContentDocument> contentDocuments = Fluxzero.queryAndWait(new GetContents(List.of(FacetFilter.builder().facetName("details/subtype").values(List.of(sighting.subtype())).build()), "", new Pagination(0, 1_000_000)));
                    if (!contentDocuments.isEmpty()) {
                        List<ContentState> contentStates = Fluxzero.queryAndWait(new GetContentStates(contentDocuments.stream().map(ContentDocument::contentId).toList(), List.of(ContentStatus.ONLINE)));
                        contentDocuments
                                .stream()
                                .filter(it -> contentStates.stream().anyMatch(state -> state.contentId().equals(it.contentId())))
                                .forEach(contentDocument -> Fluxzero.sendAndForgetCommand(new CreateWeightedAssociation(contentDocument.contentId(), new WeightedAssociationId(contentDocument.contentId(), event.sightingId()), event.sightingId(), sighting.details())));
                    }
                });
    }
}
