package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.TrackSelf;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

@TrackSelf
public interface OrderUpdate {
    @NotNull
    OrderId orderId();

    @HandleCommand
    default void handle() {
        Fluxzero.loadAggregate(orderId()).assertAndApply(this);
    }
}
