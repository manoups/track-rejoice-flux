package com.breece.proposal.command.api.model;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.UpdateLastSeenPosition;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.AcceptProposal;
import com.breece.proposal.command.api.CreateProposal;
import com.breece.proposal.command.api.DeleteLinkedProposal;
import com.breece.proposal.command.api.RejectProposal;
import com.breece.sighting.api.model.Sighting;
import com.breece.sighting.api.model.SightingId;
import com.breece.sighting.command.api.DeleteSighting;
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

import java.util.Objects;

@Stateful(commitInBatch = true)
@Consumer(name = "weighted-association-state-consumer", ignoreSegment = true)
@Slf4j
public record WeightedAssociationIdState(@EntityId WeightedAssociationId weightedAssociationId, ContentId contentId, SightingId sightingId,
                                         SightingDetails sightingDetails, @With WeightedAssociationStatus status) {
    @Association("weightedAssociationId")
    @HandleEvent
    static WeightedAssociationIdState on(CreateProposal event) {
        return new WeightedAssociationIdState(event.weightedAssociationId(), event.contentId(), event.sightingId(), event.sightingDetails(), WeightedAssociationStatus.CREATED);
    }

    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationIdState on(AcceptProposal event, Content content) {
        if (WeightedAssociationStatus.CREATED != status()) {
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

    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationIdState on(RejectProposal event) {
        if (WeightedAssociationStatus.CREATED != status()) {
            return this;
        }
        return withStatus(WeightedAssociationStatus.REJECTED);
    }

    @Association("sightingId")
    @HandleEvent
    WeightedAssociationIdState on(DeleteSighting event) {
        Fluxzero.sendAndForgetCommand(new DeleteLinkedProposal(contentId, weightedAssociationId()));
        return this;
    }

    @Association("weightedAssociationId")
    @HandleEvent
    WeightedAssociationIdState on(DeleteLinkedProposal event) {
        return null;
    }
}
