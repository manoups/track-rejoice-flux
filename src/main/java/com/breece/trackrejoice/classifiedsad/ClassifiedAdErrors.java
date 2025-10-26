package com.breece.trackrejoice.classifiedsad;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
import io.fluxzero.sdk.tracking.handling.authentication.UnauthorizedException;

public interface ClassifiedAdErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Project already exists"),
            notFound = new IllegalCommandException("Project not found"),
            unauthorized = new UnauthorizedException("Unauthorized for action"),
            taskNotFound = new IllegalCommandException("Task not found"),
            taskCompleted = new IllegalCommandException("Task has already completed");
}