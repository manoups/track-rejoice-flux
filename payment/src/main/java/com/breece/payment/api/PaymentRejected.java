package com.breece.payment.api;

import com.breece.payment.api.model.Payment;
import com.breece.payment.api.model.PaymentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRejected(@NotNull PaymentId paymentId, @NotBlank String reference) {
    @Apply
    Payment apply() {
        return new Payment(paymentId, reference,false);
    }
}
