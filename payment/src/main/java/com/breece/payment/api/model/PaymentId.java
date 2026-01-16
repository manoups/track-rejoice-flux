package com.breece.payment.api.model;

import io.fluxzero.sdk.modeling.Id;

public class PaymentId extends Id<Payment> {
    public PaymentId(String functionalId) {
        super(functionalId, "pay-");
    }
}
