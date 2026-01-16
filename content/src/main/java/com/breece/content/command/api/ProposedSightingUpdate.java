package com.breece.content.command.api;

import com.breece.common.model.Content;
import com.breece.common.model.ProposedSighting;
import com.breece.coreapi.authentication.Sender;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import jakarta.annotation.Nullable;

import java.util.Objects;

public interface ProposedSightingUpdate extends ProposedSightingCommand {
    @AssertLegal
    default void assertExists(@Nullable ProposedSighting proposedSighting) {
        if (Objects.isNull(proposedSighting)) throw ProposedSightingErrors.notFound;
    }

    @AssertLegal
    default void assertOwner(ProposedSighting proposedSighting, Sender sender) {
        Content content = Fluxzero.<Content>loadAggregateFor(proposedSighting.proposedSightingId()).get();
        if(!sender.isAuthorizedFor(content.ownerId())) {
            throw ProposedSightingErrors.unauthorized;
        }
    }
}
