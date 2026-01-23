package com.breece.order.api.command;

import com.breece.order.api.order.model.Order;
import com.breece.order.api.order.model.OrderId;
import com.breece.order.api.payment.OrderStatus;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRemoteSuccess(@NotNull OrderId orderId, String paymentReference, OrderStatus status) implements OrderUpdate{
    @Apply
    Order update(Order order) {
        return order;
    }
}
