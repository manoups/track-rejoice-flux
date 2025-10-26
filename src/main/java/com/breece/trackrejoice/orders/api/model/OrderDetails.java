package com.breece.trackrejoice.orders.api.model;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;
import com.breece.trackrejoice.payments.api.model.PaymentId;
import jakarta.validation.constraints.NotNull;
import lombok.With;

import java.time.Duration;
import java.time.Instant;

public record OrderDetails(@NotNull ClassifiedsAdId classifiedsAdId, @NotNull PaymentId paymentId, @With Instant updatedAt, @NotNull @With Duration duration) {
}
