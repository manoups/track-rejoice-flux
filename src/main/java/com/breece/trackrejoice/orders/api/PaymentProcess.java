package com.breece.trackrejoice.orders.api;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.classifiedsad.ExecutePayment;
import com.breece.trackrejoice.payments.api.PaymentConfirmed;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.Consumer;
import io.fluxzero.sdk.tracking.ForeverRetryingErrorHandler;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
@Consumer(name = "payment-consumer", ignoreSegment = true, errorHandler = ForeverRetryingErrorHandler.class)
public record PaymentProcess(@EntityId ClassifiedsAdId classifiedsAdId,
                             @Association String pspReference,
                             @With PaymentStatus status) {

    @HandleEvent
    static PaymentProcess on(PaymentInitiated event) {
        String pspRef = Fluxzero.sendCommandAndWait(new ExecutePayment());
        return new PaymentProcess(event.classifiedsAdId(), pspRef, PaymentStatus.PENDING);
    }

    @HandleEvent
    PaymentProcess on(PaymentConfirmed event) {
        // pspReference property in PaymentConfirmed is matched
        return withStatus(PaymentStatus.CONFIRMED);
    }

    @HandleEvent
    PaymentProcess on(PaymentCancelled event) {
        return null;
    }
}
