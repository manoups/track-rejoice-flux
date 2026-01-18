package com.breece.sighting.ui;


import com.breece.order.api.payment.PaymentAccepted;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.configuration.spring.ConditionalOnProperty;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Path("/payments/paypal")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "pgp", pattern = "paypal")
public class PspCallbackEndpoint {
    /*@HandlePost("/orders/{content-id}")
    void createOrder(@PathParam(value = "content-id") ContentId contentId, List<ServiceId> serviceIds) {
            Fluxzero.publishEvent(new CreateOrder(new OrderId(), contentId, new OrderDetails(serviceIds, Instant.now()), UUID.randomUUID().toString()));
    }*/

    @HandlePost("/accepted/{pspReference}")
    void captureOrder(@PathParam String pspReference) {
        Fluxzero.publishEvent(new PaymentAccepted(pspReference));
    }
}
