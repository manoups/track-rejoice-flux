package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdate;

public record DeleteLinkedProposal(ContentId contentId, WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdate {

/*    @AssertLegal
    void assertPermitted(WeightedAssociation weightedAssociation, Sender sender) {
        if (sender.nonAuthorizedFor(weightedAssociation.finder())) {
            throw WeightedProposalErrors.unauthorized;
        }
    }*/

}
