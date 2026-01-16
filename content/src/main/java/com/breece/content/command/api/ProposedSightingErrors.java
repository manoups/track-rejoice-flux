package com.breece.content.command.api;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface ProposedSightingErrors {
    FunctionalException
            notFound = new IllegalCommandException("Proposed sighting does not found"),
            unauthorized = new IllegalCommandException("Not owner of proposed sighting");
}
