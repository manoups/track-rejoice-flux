package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.authentication.RequiresRole;
import com.breece.trackrejoice.authentication.Role;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.orders.api.model.OrderId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;

@RequiresRole(Role.ADMIN)
public record AbortOrder(OrderId orderId, @NotBlank String reason) implements OrderUpdate {
    @Apply
    Order apply(Order order) {
        return order.withAborted(true);
    }
}
