package com.breece.trackrejoice.orders.api;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface OrderErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Project already exists");

}
