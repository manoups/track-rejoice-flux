package com.breece.coreapi.score.association;

import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotBlank;

@Aggregate(eventSourced = false, searchable = true)
public record WeightedAssociation(@EntityId WeightedAssociationId weightedAssociationId, @NotBlank String contentId, @NotBlank String sightingId) {
}
