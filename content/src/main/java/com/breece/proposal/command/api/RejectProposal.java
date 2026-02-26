package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdateOwner;
import com.breece.proposal.command.api.model.WeightedAssociationId;

public record RejectProposal(ContentId contentId, WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdateOwner {
}