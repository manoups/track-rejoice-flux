package com.breece.order.api;

import com.breece.content.command.api.TakeContentOffline;
import com.breece.order.api.model.Order;
import com.breece.payment.api.PaymentAccepted;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
@Consumer(name = "payment-handler")
public class PaymentHandler {
    @HandleEvent
    void handle(PaymentAccepted event) {
        Fluxzero.loadAggregate(event.reference(), Order.class).ifPresent(orderEntity ->
        {
            throw new RuntimeException("Payment accepted for order " + event.reference());
        });
    }
}
