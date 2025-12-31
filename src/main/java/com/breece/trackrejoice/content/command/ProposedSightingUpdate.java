package com.breece.trackrejoice.content.command;

import com.breece.trackrejoice.authentication.Sender;
import com.breece.trackrejoice.content.model.Content;
import com.breece.trackrejoice.content.model.ProposedSighting;
import io.fluxzero.sdk.Fluxzero;
import io.fluxzero.sdk.modeling.AssertLegal;
import io.fluxzero.sdk.tracking.handling.IllegalCommandException;
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
