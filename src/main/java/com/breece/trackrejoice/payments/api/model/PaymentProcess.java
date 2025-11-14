package com.breece.trackrejoice.payments.api.model;

import com.breece.trackrejoice.classifiedsad.ExecutePayment;
import com.breece.trackrejoice.payments.api.PaymentAccepted;
import com.breece.trackrejoice.payments.api.PaymentInitiated;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.EntityId;
import io.fluxzero.sdk.tracking.handling.Association;
import io.fluxzero.sdk.tracking.handling.HandleEvent;
import io.fluxzero.sdk.tracking.handling.Stateful;
import lombok.With;

@Stateful
public record PaymentProcess(@EntityId PaymentId paymentId,
                             @Association String pspReference,
                             @With PaymentStatus status) {

    @HandleEvent
    static PaymentProcess on(PaymentInitiated event) {
        String pspRef = Fluxzero.sendCommandAndWait(new ExecutePayment());
        return new PaymentProcess(event.paymentId(), pspRef, PaymentStatus.PENDING);
    }

    @HandleEvent
    PaymentProcess on(PaymentAccepted event) {
        // pspReference property in PaymentConfirmed is matched
        return withStatus(PaymentStatus.CONFIRMED);
    }
}