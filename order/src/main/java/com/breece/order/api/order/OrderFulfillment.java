package com.breece.order.api.order;

import com.breece.content.command.api.PublishContent;
import com.breece.coreapi.common.PaypalAuthenticate;
import com.breece.order.api.order.model.Order;
import com.breece.order.api.command.AbortOrder;
import com.breece.order.api.command.CreateOrder;
import com.breece.order.api.command.CreateOrderRemote;
import com.breece.order.api.payment.PaymentAccepted;
import com.breece.order.api.payment.PaymentRejected;
import com.breece.service.api.GetServices;
import com.breece.service.api.model.Service;
import com.breece.service.api.model.ServiceDetails;
import io.fluxzero.common.api.Metadata;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class OrderFulfillment {

    @HandleEvent
    void handle(CreateOrder event) {
        String token = Fluxzero.queryAndWait(new PaypalAuthenticate());
        List<ServiceDetails> list = Fluxzero.queryAndWait(new GetServices()).stream().filter(service -> event.details().serviceIds().contains(service.serviceId())).map(Service::serviceDetails).toList();
//        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(this::authenticate, 30, TimeUnit.SECONDS);
        Fluxzero.sendAndForgetCommand(new CreateOrderRemote(event.orderId(), event.pspReference(), list), Metadata.of("token", token));
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
        Order order = Fluxzero.loadEntityValue(event.reference());
        Fluxzero.sendAndForgetCommand(new PublishContent(order.contentId(), Duration.ofDays(90)));
    }
}
