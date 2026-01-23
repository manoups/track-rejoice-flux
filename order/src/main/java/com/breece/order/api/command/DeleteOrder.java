package com.breece.order.api.command;

import com.breece.order.api.order.model.Order;
import com.breece.order.api.order.model.OrderId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record DeleteOrder(OrderId orderId) implements OrderUpdate {
    @Apply
    Order delete(Order order) {
        return null;
    }
}
