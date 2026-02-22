package com.breece.content.command.api;

import com.breece.content.query.api.ContentDocument;
import com.breece.content.query.api.GetContents;
import com.breece.coreapi.facets.Pagination;
import com.breece.coreapi.score.association.CreateWeightedAssociation;
import com.breece.coreapi.score.association.DeleteWeightedAssociation;
import com.breece.coreapi.score.association.WeightedAssociationId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.GetSightings;
import com.breece.sighting.query.api.SightingDocument;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Consumer(name = "weighted-association-handler", ignoreSegment = true)
public class WeightedAssociationHandler {
//    TODO: Check GetRelationships and GetAggregateIds
    @HandleEvent
    void on(CreateSighting event) {
        List<ContentDocument> contentDocuments = Fluxzero.queryAndWait(new GetContents(List.of(), null, new Pagination(0, 1_000_000_000)));
        for (ContentDocument contentDocument : contentDocuments) {
            Fluxzero.sendAndForgetCommand(new CreateWeightedAssociation(new WeightedAssociationId(contentDocument.contentId().getId(), event.sightingId().getId()), contentDocument.contentId().getId(), event.sightingId().getId()));
        }
    }

    @HandleEvent
    void on(DeleteSighting event) {
        List<ContentDocument> contentDocuments = Fluxzero.queryAndWait(new GetContents(List.of(), null, new Pagination(0, 1_000_000_000)));
        for (ContentDocument contentDocument : contentDocuments) {
            Fluxzero.sendAndForgetCommand(new DeleteWeightedAssociation(new WeightedAssociationId(contentDocument.contentId().getId(), event.sightingId().getId())));
        }
    }

    @HandleEvent
    void on(CreateContent event) {
        List<SightingDocument> sightingDocuments = Fluxzero.queryAndWait(new GetSightings(List.of(), null, new Pagination(0, 1_000_000_000)));
        for (SightingDocument sightingDocument : sightingDocuments) {
            Fluxzero.sendAndForgetCommand(new CreateWeightedAssociation(new WeightedAssociationId(event.contentId().getId(), sightingDocument.sightingId().getId()), event.contentId().getId(), sightingDocument.sightingId().getId()));
        }
    }

    @HandleEvent
    void on(DeleteContent event) {
        List<SightingDocument> sightingDocuments = Fluxzero.queryAndWait(new GetSightings(List.of(), null, new Pagination(0, 1_000_000_000)));
        for (SightingDocument sightingDocument : sightingDocuments) {
            Fluxzero.sendAndForgetCommand(new DeleteWeightedAssociation(new WeightedAssociationId(event.contentId().getId(), sightingDocument.sightingId().getId())));
        }
    }
}
