package com.breece.proposal.api;

import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.api.model.*;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;
import io.fluxzero.sdk.persisting.eventsourcing.InterceptApply;
import jakarta.validation.constraints.NotNull;

public record DeleteLinkedProposal(@NotNull LinkedSightingId linkedSightingId) implements LinkedSightingUpdate {
    @InterceptApply
    DeleteLinkedProposal assertNotLinked(LinkedSighting linkedSighting) {
        if (Fluxzero.getDocument(linkedSighting.linkedSightingId(), LinkedSightingState.class).get().status()== LinkedSightingStatus.ACCEPTED) {
            return null;
        }
        return this;
    }

    @AssertLegal
    void assertPermitted(LinkedSighting linkedSighting, Sender sender) {
        if (sender.nonAuthorizedFor(linkedSighting.finder())) {
            throw LinkedSightingErrors.unauthorized;
        }
    }

    @Apply
    LinkedSighting apply(LinkedSighting linkedSighting) {
        return null;
    }
}
