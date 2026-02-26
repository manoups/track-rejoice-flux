package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdateOwner;

public record AcceptProposal(ContentId contentId, WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdateOwner {
}
