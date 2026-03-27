package com.breece.proposal.command.api;

import com.breece.content.api.model.Content;
import com.breece.content.api.model.ContentId;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationUpdate;
import io.fluxzero.sdk.modeling.EventPublicationStrategy;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record DeleteWeightedAssociation(ContentId contentId, WeightedAssociationId weightedAssociationId) implements WeightedAssociationUpdate {
    @Apply(publicationStrategy = EventPublicationStrategy.PUBLISH_ONLY)
    void apply(Content content) {}
}
