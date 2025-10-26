package com.breece.trackrejoice.payments.api;

import com.breece.trackrejoice.payments.api.model.Payment;
import com.breece.trackrejoice.payments.api.model.PaymentId;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRejected(@NotNull PaymentId paymentId, @NotBlank String reference) {
    @Apply
    Payment apply() {
        return new Payment(paymentId, reference,false);
    }
}
