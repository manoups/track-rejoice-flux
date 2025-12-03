package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.common.search.Facet;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import lombok.With;

@Aggregate(searchable = true)
public record ClassifiedsAd(@EntityId ClassifiedsAdId classifiedsAdId, @With @Facet ExtraDetails details, UserId ownerId, @With boolean online) {
}
