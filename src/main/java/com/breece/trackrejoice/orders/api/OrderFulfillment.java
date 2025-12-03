package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.classifiedsad.PublishClassifiedsAd;
import com.breece.trackrejoice.common.PaypalAuthenticate;
import com.breece.trackrejoice.orders.api.command.AbortOrder;
import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import com.breece.trackrejoice.orders.api.command.ValidateOrder;
import com.breece.trackrejoice.orders.api.model.Order;
import com.breece.trackrejoice.payments.api.PaymentAccepted;
import com.breece.trackrejoice.payments.api.PaymentRejected;
import io.fluxzero.common.api.Metadata;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

@Component
public class OrderFulfillment {

    @HandleEvent
    void handle(PlaceOrder event) {
        String token = Fluxzero.queryAndWait(new PaypalAuthenticate());
//        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(this::authenticate, 30, TimeUnit.SECONDS);
        Fluxzero.sendAndForgetCommand(new ValidateOrder(event.orderId(), event.orderId().toString()), Metadata.of("token", token));
    }

    /*private String authenticate() {
        return Fluxzero.queryAndWait(new PaypalAuthenticate());
    }*/

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
