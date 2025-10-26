package com.breece.trackrejoice.payments.api;

import com.breece.trackrejoice.classifiedsad.model.ClassifiedsAdId;

public record PaymentConfirmed(ClassifiedsAdId classifiedsAdId, String pspReference) {
}
