package com.breece.order.api.command;

import com.breece.order.api.model.OrderId;
import com.breece.order.api.model.Order;
import com.breece.coreapi.authentication.RequiresRole;
import com.breece.coreapi.authentication.Role;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

@RequiresRole(Role.ADMIN)
public record UpdateOrder(@NotNull OrderId orderId, String status) implements OrderUpdate{
    @Apply
    public Order apply(Order order) {
        return order.withStatus(status);
    }
}
