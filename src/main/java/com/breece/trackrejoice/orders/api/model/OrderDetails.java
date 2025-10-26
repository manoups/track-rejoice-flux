package com.breece.trackrejoice.orders.api.model;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.payments.api.model.PaymentId;
import jakarta.validation.constraints.NotNull;

public record OrderDetails(@NotNull ClassifiedsAdId classifiedsAdId, PaymentId paymentId) {
}
