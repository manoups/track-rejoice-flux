package com.breece.order.api.payment;

import com.breece.order.api.command.CreateOrderRemote;
import com.breece.order.api.order.model.OrderId;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
@Consumer(name = "order-consumer", ignoreSegment = true)
public record OrderProcess(@EntityId OrderId orderId,
                           @Association String pspReference,
                           @With OrderStatus status) {

    @HandleEvent
    static OrderProcess on(CreateOrderRemote event) {
//        String pspRef = Fluxzero.sendCommandAndWait(new ExecutePayment());
        return new OrderProcess(event.orderId(), event.pspReference(), OrderStatus.CREATED);
    }

    @HandleEvent
    OrderProcess on(PaymentAccepted event) {
        // pspReference property in PaymentConfirmed is matched
        return withStatus(OrderStatus.APPROVED);
    }
}