package com.breece.order.api.payment;

import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import org.springframework.stereotype.Component;

@Component
@Path("/payments")
public class PaymentEndpoint {
    @HandlePost(value = {"/accepted/{orderId}/{pspReference}","/accepted/{orderId}/{pspReference}/"})
    void paymentAccepted(@PathParam String pspReference) {
        Fluxzero.publishEvent(new PaymentAccepted(pspReference));
    }
}
