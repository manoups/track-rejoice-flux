package com.breece.proposal.command.api;

import com.breece.content.api.model.ContentId;
import com.breece.coreapi.authentication.Sender;
import com.breece.proposal.command.api.model.LinkedSighting;
import com.breece.proposal.command.api.model.LinkedSightingId;
import com.breece.proposal.command.api.model.LinkedSightingUpdate;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.persisting.eventsourcing.Apply;

public record DeleteLinkedProposal(ContentId contentId, LinkedSightingId linkedSightingId) implements LinkedSightingUpdate {

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
