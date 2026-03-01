package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdateOwner;
import io.fluxzero.sdk.modeling.EventPublicationStrategy;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record AcceptProposal(ContentId contentId,
                             WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdateOwner {
    @Apply(publicationStrategy = EventPublicationStrategy.PUBLISH_ONLY)
    void apply() {}
}
