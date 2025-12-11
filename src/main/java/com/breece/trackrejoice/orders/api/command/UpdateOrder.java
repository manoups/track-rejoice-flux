package com.breece.trackrejoice.orders.api.command;

import com.breece.trackrejoice.authentication.RequiresRole;
import com.breece.trackrejoice.authentication.Role;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

@RequiresRole(Role.ADMIN)
public record UpdateOrder(@NotNull OrderId orderId, String status) implements OrderUpdate{
    @Apply
    public Order apply(Order order) {
        return order.withStatus(status);
    }
}
