package com.breece.order.api.command;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.publishing.routing.RoutingKey;
import io.fluxzero.sdk.tracking.handling.HandleCommand;
import com.breece.order.api.model.OrderId;
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
