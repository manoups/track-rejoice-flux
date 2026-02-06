package com.breece.sighting.api.model;

import com.breece.coreapi.common.SightingDetails;
import com.breece.coreapi.user.WithOwner;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Aggregate(searchable = true)
@Builder(toBuilder = true)
public record Sighting(@EntityId SightingId sightingId, @NotNull UserId ownerId, @NotNull SightingDetails details, @With boolean removeAfterMatching) implements WithOwner {
}
