package com.breece.trackrejoice.payments.api;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.orders.api.command.PlaceOrder;
import com.breece.trackrejoice.orders.api.model.OrderDetails;
import com.breece.trackrejoice.orders.api.model.OrderId;
import com.breece.trackrejoice.payments.api.model.PaymentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.configuration.spring.ConditionalOnProperty;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@Path("/payments/paypal")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "pgp", pattern = "paypal")
public class PayPalEndpoint {
    @HandlePost("/orders/{classifiedsAdId}")
    void createOrder(@PathParam ClassifiedsAdId classifiedsAdId) {
            Fluxzero.publishEvent(new PlaceOrder(new OrderId(), new OrderDetails(classifiedsAdId, null, Instant.now(), Duration.ofDays(90))));
    }

    @HandlePost("/orders/{orderID}/{announcementId}/capture")
    void captureOrder(@PathParam OrderId orderID, @PathParam String announcementId) {
        Fluxzero.publishEvent(new PaymentId(orderID.getFunctionalId()));
    }
}
