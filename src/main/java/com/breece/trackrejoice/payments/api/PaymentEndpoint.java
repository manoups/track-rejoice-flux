package com.breece.trackrejoice.payments.api;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.web.HandlePost;
import io.fluxzero.sdk.web.Path;
import io.fluxzero.sdk.web.PathParam;
import org.springframework.stereotype.Component;

@Component
@Path("/payments")
public class PaymentEndpoint {
    @HandlePost(value = {"/accepted/{classifiedsAdId}/{pspReference}","/accepted/{classifiedsAdId}/{pspReference}/"})
    void paymentAccepted(@PathParam ClassifiedsAdId classifiedsAdId, @PathParam String pspReference) {
        Fluxzero.publishEvent(new PaymentConfirmed(classifiedsAdId, pspReference));
    }
}
