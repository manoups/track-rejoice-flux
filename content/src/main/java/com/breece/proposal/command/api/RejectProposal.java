package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdateOwner;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import io.fluxzero.sdk.modeling.EventPublicationStrategy;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record RejectProposal(ContentId contentId, WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdateOwner {
    @Apply(publicationStrategy = EventPublicationStrategy.PUBLISH_ONLY)
    void apply(Content content) {}
}