package com.breece.trackrejoice.content.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import lombok.With;

@Aggregate(searchable = true)
public record Content(@EntityId ContentId contentId, @With @Facet ExtraDetails details, UserId ownerId,
                      @With boolean online) {
}
