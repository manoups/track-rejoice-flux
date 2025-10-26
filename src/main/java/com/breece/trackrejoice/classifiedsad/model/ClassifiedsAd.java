package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.user.api.UserId;
import io.fluxzero.sdk.modeling.Aggregate;
import io.fluxzero.sdk.modeling.EntityId;
import lombok.With;

@Aggregate(searchable = true)
public record ClassifiedsAd(@EntityId ClassifiedsAdId classifiedsAdId, @With ExtraDetails details, UserId ownerId, @With boolean online) {
}
