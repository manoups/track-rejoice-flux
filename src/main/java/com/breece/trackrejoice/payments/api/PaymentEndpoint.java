package com.breece.trackrejoice.payments.api;

import com.breece.trackrejoice.payments.api.model.PaymentId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import org.springframework.stereotype.Component;

@Component
@Path("/payments")
public class PaymentEndpoint {
    @HandlePost(value = {"/accepted/{paymentId}/{pspReference}","/accepted/{paymentId}/{pspReference}/"})
    void paymentAccepted(@PathParam PaymentId paymentId, @PathParam String pspReference) {
        Fluxzero.publishEvent(new PaymentAccepted(paymentId, pspReference));
    }
}
