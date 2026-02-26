package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentInteract;
import com.breece.coreapi.authentication.Sender;
import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.model.WeightedAssociationCommand;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationState;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public record ClaimSighting(@NotNull ContentId contentId, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails, @NotNull WeightedAssociationId weightedAssociationId) implements ContentInteract {

    @InterceptApply
    WeightedAssociationCommand interceptApply(Content content, Sender sender) {
        WeightedAssociationState weightedAssociationState = Fluxzero.queryAndWait(new GetWeightedAssociationState(weightedAssociationId));
        if (Objects.isNull(weightedAssociationState)) {
            throw new IllegalArgumentException("Weighted Association not found");
        } else if (WeightedAssociationStatus.LINKED == weightedAssociationState.status()) {
            return new AcceptProposal(contentId, weightedAssociationId);
        }
        return new CreateProposal(contentId, weightedAssociationId);
    }
}
