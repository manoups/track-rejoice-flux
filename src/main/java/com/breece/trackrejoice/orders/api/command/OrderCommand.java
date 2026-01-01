package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.publishing.routing.RoutingKey;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import jakarta.validation.constraints.NotNull;

public interface OrderCommand {
    @RoutingKey
    @NotNull
    OrderId orderId();

    @HandleCommand
    default void handle() {
        Fluxzero.loadAggregate(orderId()).assertAndApply(this);
    }
}
