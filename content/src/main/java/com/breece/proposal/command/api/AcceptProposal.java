package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.content.api.model.WeightedAssociation;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.proposal.command.api.model.WeightedAssociationUpdateOwner;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record AcceptProposal(ContentId contentId,
                             WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdateOwner {

    @AssertLegal
    void assertProperState(WeightedAssociation weightedAssociation) {
        if (WeightedAssociationStatus.LINKED != weightedAssociation.status()) {
            throw WeightedAssociationErrors.incorrectState;
        }
    }
    @Apply
    WeightedAssociation apply(WeightedAssociation weightedAssociation) {
        return weightedAssociation.withStatus(WeightedAssociationStatus.ACCEPTED);
    }
}
