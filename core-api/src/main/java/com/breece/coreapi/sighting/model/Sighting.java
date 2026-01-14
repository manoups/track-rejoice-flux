package com.breece.coreapi.sighting.model;

import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Aggregate(searchable = true)
@Builder(toBuilder = true)
public record Sighting(@EntityId SightingId sightingId, @NotNull UserId source, @NotNull SightingDetails details,
                       @Singular List<ContentId> linkedContents) {
}
