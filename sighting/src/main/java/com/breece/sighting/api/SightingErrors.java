package com.breece.sighting.api;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;

public interface SightingErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Sighting already exists"),
            notFound = new IllegalCommandException("Sighting not found"),
            notOwner = new IllegalCommandException("Not ownerId of sighting"),
            unauthorized = new UnauthorizedException("Unauthorized action"),
            sightingMismatch = new IllegalCommandException("Sighting details do not match");
}
