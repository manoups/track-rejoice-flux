package com.breece.trackrejoice.sighting;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface SightingErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Sighting already exists"),
            notFound = new IllegalCommandException("Sighting not found"),
            alreadyClaimed = new IllegalCommandException("Sighting already claimed"),
            contentNotFound = new IllegalCommandException("Content not found"),
            notOwner = new IllegalCommandException("Not owner of sighting"),
            unauthorized = new IllegalCommandException("Unauthorized action");
}
