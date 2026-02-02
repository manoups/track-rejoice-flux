package com.breece.proposal.api.model;


import com.breece.content.api.model.ContentId;
import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;
import lombok.With;

@Aggregate(searchable = true)
public record LinkedSighting(@EntityId LinkedSightingId linkedSightingId, @NotNull UserId finder,
                             @NotNull UserId seeker, @NotNull SightingId sightingId, @NotNull ContentId contentId,
                             @NotNull SightingDetails sightingDetails) {
}
