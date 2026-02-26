package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.content.command.api.ContentCommand;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.WeightedAssociation;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdate;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;

import java.util.List;

public record CreateProposal(ContentId contentId, WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdate {
    @InterceptApply
    List<ContentCommand> interceptApply(Content content, Sender sender) {
        if (sender.isAuthorizedFor(content.ownerId())) {
            return List.of(this, new AcceptProposal(contentId, weightedAssociationId()));
        }
        return List.of(this);
    }
}
