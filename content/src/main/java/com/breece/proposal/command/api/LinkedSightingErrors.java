package com.breece.proposal.command.api;

import io.fluxzero.sdk.common.exception.FunctionalException;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;

public interface LinkedSightingErrors {
    FunctionalException
            alreadyExists = new IllegalCommandException("Linked sighting already exists"),
            notFound = new IllegalCommandException("Linked sighting not found"),
            unauthorized = new IllegalCommandException("Not owner of linked sighting"),
            linkedProposalCannotBeDeleted = new IllegalCommandException("Linked proposal cannot be deleted"),
            malformedKey = new IllegalCommandException("Linked sighting id malformed"),
            incorrectState = new IllegalCommandException("Linked sighting is in incorrect status");
}
