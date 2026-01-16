package com.breece.content;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;

public interface ContentErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Content already exists"),
            notFound = new IllegalCommandException("Content not found"),
            unauthorized = new UnauthorizedException("Unauthorized for action"),
            taskNotFound = new IllegalCommandException("Task not found"),
            taskCompleted = new IllegalCommandException("Task has already completed"),
            sightingNotFound = new IllegalCommandException("Sighting not found");
}