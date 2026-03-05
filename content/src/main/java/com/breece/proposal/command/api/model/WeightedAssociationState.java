package com.breece.proposal.command.api.model;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.ContentStatus;
import com.breece.content.command.api.*;
import com.breece.content.query.api.ContentDocument;
import com.breece.content.query.api.GetContents;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.facets.FacetFilter;
import com.breece.coreapi.facets.Pagination;
import com.breece.coreapi.util.GeometryUtil;
import com.breece.proposal.command.api.AcceptProposal;
import com.breece.proposal.command.api.CreateProposal;
import com.breece.proposal.command.api.DeleteWeightedAssociation;
import com.breece.proposal.command.api.RejectProposal;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.CreateSighting;
import com.breece.sighting.command.api.DeleteSighting;
import com.breece.sighting.query.api.GetSightings;
import com.breece.sighting.query.api.SightingDocument;
import io.fluxzero.common.search.Facet;
import io.fluxzero.common.search.Sortable;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.Entity;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import io.fluxzero.sdk.tracking.handling.Stateful;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Stateful(commitInBatch = true)
@Consumer(name = "weighted-association-state-consumer", ignoreSegment = true)
@Slf4j
public record WeightedAssociationState(@EntityId WeightedAssociationId weightedAssociationId, ContentId contentId,
                                       SightingId sightingId,
                                       SightingDetails sightingDetails,
                                       @With @Facet WeightedAssociationStatus status, @With double distance, @With @Sortable double score) {
    @Association("contentId")
    @HandleEvent
    static List<WeightedAssociationState> on(PublishContent event, Content content) {
        List<SightingDocument> sightingDocuments = Fluxzero.queryAndWait(new GetSightings(List.of(FacetFilter.builder().facetName("subtype").values(List.of(content.details().getSubtype())).build()), "", new Pagination(0, 1_000_000)));
        return sightingDocuments.stream().map(sightingDocument ->
                {
                    double distance = GeometryUtil.parseLocation(content.lastConfirmedSighting().lat(), content.lastConfirmedSighting().lng()).distance(GeometryUtil.parseLocation(sightingDocument.details().lat(), sightingDocument.details().lng()));
                    return new WeightedAssociationState(new WeightedAssociationId(content.contentId(), sightingDocument.sightingId()), event.contentId(), sightingDocument.sightingId(), sightingDocument.details(), WeightedAssociationStatus.CREATED, distance, distance);
                }
        ).toList();
    }

    @Association("sightingId")
    @HandleEvent
    static List<WeightedAssociationState> on(CreateSighting event, Sighting sighting) {
        List<ContentDocument> contentDocuments = Fluxzero.queryAndWait(new GetContents(List.of(FacetFilter.builder().facetName("details/subtype").values(List.of(sighting.subtype())).build()), "", new Pagination(0, 1_000_000)));
        if (!contentDocuments.isEmpty()) {
            List<ContentState> contentStates = Fluxzero.queryAndWait(new GetContentStates(contentDocuments.stream().map(ContentDocument::contentId).toList(), List.of(ContentStatus.ONLINE)));
            return contentDocuments
                    .stream()
                    .filter(it -> contentStates.stream().anyMatch(state -> state.contentId().equals(it.contentId())))
                    .map(contentDocument -> {
                        double distance = GeometryUtil.parseLocation(contentDocument.lastConfirmedSighting().lat(), contentDocument.lastConfirmedSighting().lng()).distance(GeometryUtil.parseLocation(sighting.details().lat(), sighting.details().lng()));
                        return new WeightedAssociationState(new WeightedAssociationId(contentDocument.contentId(), event.sightingId()), contentDocument.contentId(), event.sightingId(), sighting.details(), WeightedAssociationStatus.CREATED,
                                distance, distance);
                    })
                    .toList();
        }
        return null;
    }

    /*@Association("weightedAssociationId")
    @HandleEvent
    static WeightedAssociationState on(CreateWeightedAssociation event) {
        return new WeightedAssociationState(event.weightedAssociationId(), event.contentId(), event.sightingId(), event.sightingDetails(), WeightedAssociationStatus.CREATED);
    }*/

    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationState on(CreateProposal event) {
        if (WeightedAssociationStatus.CREATED != status()) {
            return this;
        }
        return withStatus(WeightedAssociationStatus.LINKED);
    }

    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationState on(AcceptProposal event, Content content) {
        if (WeightedAssociationStatus.LINKED != status()) {
            return this;
        }
        DeleteSighting deleteSighting = Fluxzero.loadAggregate(sightingId())
                .mapIfPresent(Entity::get)
                .filter(Sighting::removeAfterMatching)
                .map(Sighting::sightingId)
                .map(DeleteSighting::new).orElse(null);
        if (Objects.nonNull(deleteSighting)) {
            try {
                Fluxzero.sendCommandAndWait(deleteSighting);
            } catch (IllegalCommandException | UnauthorizedException e) {
                return this;
            }
        }
        Fluxzero.sendAndForgetCommand(new UpdateLastSeenPosition(content.contentId(), sightingDetails()));

        return withStatus(WeightedAssociationStatus.ACCEPTED);
    }

    @Association("contentId")
    @HandleEvent
    WeightedAssociationState on(UpdateLastSeenPosition event) {
        double distance = GeometryUtil.parseLocation(event.sightingDetails().lat(), event.sightingDetails().lng()).distance(GeometryUtil.parseLocation(sightingDetails().lat(), sightingDetails().lng()));
        return withDistance(distance).withScore(distance);
    }

    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationState on(RejectProposal event) {
        if (WeightedAssociationStatus.LINKED != status()) {
            return this;
        }
        return withStatus(WeightedAssociationStatus.REJECTED);
    }

    @Association("sightingId")
    @HandleEvent
    WeightedAssociationState on(DeleteSighting event) {
        Fluxzero.sendAndForgetCommand(new DeleteWeightedAssociation(contentId, weightedAssociationId()));
        return this;
    }

    @Association("contentId")
    @HandleEvent
    WeightedAssociationState on(TakeContentOffline event, Content content) {
        Fluxzero.sendAndForgetCommand(new DeleteWeightedAssociation(contentId, weightedAssociationId()));
        return this;
    }

    @Association("contentId")
    @HandleEvent
    WeightedAssociationState on(DeleteContent event, Content content) {
        Fluxzero.sendAndForgetCommand(new DeleteWeightedAssociation(contentId, weightedAssociationId()));
        return this;
    }


    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationState on(DeleteWeightedAssociation event) {
        return null;
    }
}
