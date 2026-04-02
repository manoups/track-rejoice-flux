package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.WeightedAssociation;
import com.breece.content.command.api.ContentInteract;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.proposal.command.api.model.WeightedAssociationUpdate;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import jakarta.validation.constraints.NotNull;

public record ClaimSighting(@NotNull ContentId contentId, @NotNull WeightedAssociationId weightedAssociationId) implements ContentInteract {

    @InterceptApply
    WeightedAssociationUpdate interceptApply(Content content, Sender sender) {
        WeightedAssociation weightedAssociation = content.weightedAssociations().stream().filter(wa -> wa.weightedAssociationId().equals(weightedAssociationId)).findFirst().orElseThrow(() -> WeightedAssociationErrors.notFound);
//        WeightedAssociationState weightedAssociationState = Fluxzero.queryAndWait(new GetWeightedAssociationState(weightedAssociationId));
//        if (Objects.isNull(weightedAssociationState)) {
//            throw WeightedAssociationErrors.notFound;
        if (WeightedAssociationStatus.LINKED == weightedAssociation.status()) {
            return new AcceptProposal(contentId, weightedAssociationId);
        }
        return new CreateProposal(contentId, weightedAssociationId);
    }
}
