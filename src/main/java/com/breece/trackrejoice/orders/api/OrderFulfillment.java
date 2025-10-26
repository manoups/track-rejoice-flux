package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.classifiedsad.PublishClassifiedsAd;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.payments.api.PaymentAccepted;
import com.breece.trackrejoice.payments.api.PaymentRejected;
import com.breece.trackrejoice.payments.api.ValidatePayment;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderFulfillment {

    @HandleEvent
    void handle(PlaceOrder event) {
        Fluxzero.sendAndForgetCommand(new ValidatePayment(event.details().paymentId(), event.orderId().toString()));
    }

    @HandleEvent
    void handle(PaymentRejected event) {
        var order = Fluxzero.<Order>loadEntityValue(event.reference());
        Fluxzero.sendAndForgetCommand(new AbortOrder(order.orderId(), "Payment rejected"));
    }

    @HandleEvent
    void handle(PaymentAccepted event) {
        var order = Fluxzero.<Order>loadEntityValue(event.reference());
        Fluxzero.sendAndForgetCommand(new PublishClassifiedsAd(order.details().classifiedsAdId(), order.orderId()));
    }
}
