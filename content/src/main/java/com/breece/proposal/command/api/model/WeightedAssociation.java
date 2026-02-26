package com.breece.proposal.command.api.model;


import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.api.UserId;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;

public record WeightedAssociation(@EntityId WeightedAssociationId weightedAssociationId, @NotNull UserId finder, @NotNull SightingId sightingId, @NotNull SightingDetails sightingDetails) {
}
