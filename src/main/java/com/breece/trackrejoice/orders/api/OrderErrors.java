package com.breece.trackrejoice.orders.api;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface OrderErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Order already exists"),
            alreadyPlaced = new IllegalCommandException("Order has already been placed"),
            notFound = new IllegalCommandException("Order not found"),
            tooOld = new IllegalCommandException("Order cancellation expired"),
            productNotFound = new IllegalCommandException("Product not found"),
            jsonProcessingException = new IllegalCommandException("JsonProcessingException");
}
