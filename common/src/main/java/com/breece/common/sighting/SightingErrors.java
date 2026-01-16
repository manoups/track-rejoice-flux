package com.breece.common.sighting;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface SightingErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Sighting already exists"),
            notFound = new IllegalCommandException("Sighting not found"),
            notLinkedToContent = new IllegalCommandException("Sighting not linked to content"),
            notOwner = new IllegalCommandException("Not source of sighting"),
            unauthorized = new IllegalCommandException("Unauthorized action"),
            sightingMismatch = new IllegalCommandException("Sighting details do not match"),
            alreadyProposed = new IllegalCommandException("Sighting already proposed");
}
