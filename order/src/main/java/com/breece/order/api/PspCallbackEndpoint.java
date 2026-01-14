package com.breece.order.api;


import com.breece.coreapi.content.model.ContentId;
import com.breece.coreapi.order.model.OrderDetails;
import com.breece.coreapi.order.model.OrderId;
import com.breece.coreapi.payment.model.PaymentId;
import com.breece.coreapi.service.model.ServiceId;
import com.breece.order.api.command.PlaceOrder;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.configuration.spring.ConditionalOnProperty;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@Path("/payments/paypal")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "pgp", pattern = "paypal")
public class PspCallbackEndpoint {
    @HandlePost("/orders/{content-id}")
    void createOrder(@PathParam(value = "content-id") ContentId contentId, List<ServiceId> serviceIds) {
            Fluxzero.publishEvent(new PlaceOrder(new OrderId(), contentId, new OrderDetails(serviceIds, Instant.now(), Duration.ofDays(90))));
    }

    @HandlePost("/orders/{orderID}/{announcementId}/capture")
    void captureOrder(@PathParam OrderId orderID, @PathParam String announcementId) {
        Fluxzero.publishEvent(new PaymentId(orderID.getFunctionalId()));
    }
}
