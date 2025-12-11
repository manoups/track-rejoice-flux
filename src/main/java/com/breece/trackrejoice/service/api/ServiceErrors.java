package com.breece.trackrejoice.service.api;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface ServiceErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Service already exists"),
            alreadyPlaced = new IllegalCommandException("Service has already been placed"),
            notFound = new IllegalCommandException("Service not found"),
            tooOld = new IllegalCommandException("Service cancellation expired"),
            productNotFound = new IllegalCommandException("Product not found");
}
