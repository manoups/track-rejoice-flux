package com.breece.content.api.model;

import com.breece.coreapi.common.SightingDetails;
import com.breece.proposal.command.api.model.WeightedAssociationId;
import com.breece.proposal.command.api.model.WeightedAssociationStatus;
import com.breece.sighting.api.model.SightingId;
import io.fluxzero.sdk.modeling.EntityId;

public record WeightedAssociation(@EntityId WeightedAssociationId weightedAssociationId, SightingId sightingId, SightingDetails sightingDetails, WeightedAssociationStatus status) {
}
