package com.breece.order.api.payment;

import com.breece.order.api.command.AbortOrder;
import com.breece.order.api.command.CreateOrderRemoteSuccess;
import com.breece.order.api.command.DeleteOrder;
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
                           @Association String paymentReference,
                           @With OrderStatus status) {

    @HandleEvent
    static OrderProcess on(CreateOrderRemoteSuccess event) {
//        String pspRef = Fluxzero.sendCommandAndWait(new ExecutePayment());
        return new OrderProcess(event.orderId(), event.paymentReference(), OrderStatus.CREATED);
    }

    @HandleEvent
    OrderProcess on(PaymentAccepted event) {
        // pspReference property in PaymentConfirmed is matched
        return withStatus(OrderStatus.APPROVED);
    }

    @HandleEvent
    OrderProcess on(AbortOrder event) {
        return  withStatus(OrderStatus.VOIDED);
    }

    @HandleEvent
    OrderProcess on(DeleteOrder order) {
        return null;
    }
}