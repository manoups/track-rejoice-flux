package com.breece.payment.api;

import com.breece.payment.api.model.PaymentId;
import com.breece.payment.api.model.PaymentStatus;
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