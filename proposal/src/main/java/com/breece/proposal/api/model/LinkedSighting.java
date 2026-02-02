package com.breece.proposal.api.model;


import com.breece.content.api.model.ContentId;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.modeling.EventPublicationStrategy;
import jakarta.validation.constraints.NotNull;

@Aggregate(searchable = true, publicationStrategy = EventPublicationStrategy.PUBLISH_ONLY)
public record LinkedSighting(@EntityId LinkedSightingId linkedSightingId, @NotNull UserId finder,
                             @NotNull UserId seeker, @NotNull SightingId sightingId, @NotNull ContentId contentId,
                             @NotNull SightingDetails sightingDetails) {
}
