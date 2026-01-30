package com.breece.sighting.api.model;

import com.breece.coreapi.user.WithOwner;
import com.breece.coreapi.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Aggregate(searchable = true)
@Builder(toBuilder = true)
public record Sighting(@EntityId SightingId sightingId, @NotNull UserId source, @NotNull com.breece.coreapi.common.SightingDetails details,
                       @Singular List<String> linkedContents) implements WithOwner {
    @Override
    public UserId ownerId() {
        return source;
    }
}
