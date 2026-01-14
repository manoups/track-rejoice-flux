package com.breece.coreapi.payment.model;

import io.fluxzero.sdk.modeling.Id;

public class PaymentId extends Id<Payment> {
    public PaymentId(String functionalId) {
        super(functionalId, "pay-");
    }
}
