package com.breece.trackrejoice.classifiedsad.model;

import com.breece.trackrejoice.classifiedsad.command.CreateClassifiedsAd;
import com.breece.trackrejoice.classifiedsad.command.DeleteClassifiedsAd;
import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import com.breece.trackrejoice.orders.api.command.ValidateOrder;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
@Consumer(name = "classifieds-ads-consumer", ignoreSegment = true)
public record ClassifiedsAdState(@Association @EntityId ClassifiedsAdId classifiedsAdId, @With ClassifiedsAdStatus status) {

    @HandleEvent
    static ClassifiedsAdState on(CreateClassifiedsAd event) {
        return new ClassifiedsAdState(event.classifiedsAdId(), ClassifiedsAdStatus.DRAFT);
    }

    @HandleEvent
    ClassifiedsAdState on(PlaceOrder order) {
        return withStatus(ClassifiedsAdStatus.PENDING_PAYMENT);
    }

    @HandleEvent
    ClassifiedsAdState on(ValidateOrder order) {
        return withStatus(ClassifiedsAdStatus.ENABLED);
    }

    @HandleEvent
    ClassifiedsAdState on(DeleteClassifiedsAd order) {
        return null;
    }
}
